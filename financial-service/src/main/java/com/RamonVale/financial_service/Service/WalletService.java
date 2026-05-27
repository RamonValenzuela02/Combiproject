package com.RamonVale.financial_service.Service;

import com.RamonVale.financial_service.Domain.wallet.Advance;
import com.RamonVale.financial_service.Domain.wallet.AdvanceStatus;
import com.RamonVale.financial_service.Domain.wallet.CardHold;
import com.RamonVale.financial_service.Domain.wallet.CorporateAccount;
import com.RamonVale.financial_service.Domain.wallet.EmployeeQuota;
import com.RamonVale.financial_service.Domain.wallet.VirtualCard;
import com.RamonVale.financial_service.Repository.AdvanceRepository;
import com.RamonVale.financial_service.Repository.CardHoldRepository;
import com.RamonVale.financial_service.Repository.CorporateAccountRepository;
import com.RamonVale.financial_service.Repository.EmployeeQuotaRepository;
import com.RamonVale.financial_service.Repository.VirtualCardRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

  private static final Logger log = LoggerFactory.getLogger(WalletService.class);

  private final AdvanceRepository advanceRepo;
  private final CardHoldRepository holdRepo;
  private final VirtualCardRepository cardRepo;
  private final CorporateAccountRepository corpRepo;
  private final EmployeeQuotaRepository quotaRepo;

  public WalletService(AdvanceRepository advanceRepo,
                       CardHoldRepository holdRepo,
                       VirtualCardRepository cardRepo,
                       CorporateAccountRepository corpRepo,
                       EmployeeQuotaRepository quotaRepo) {
    this.advanceRepo = advanceRepo;
    this.holdRepo    = holdRepo;
    this.cardRepo    = cardRepo;
    this.corpRepo    = corpRepo;
    this.quotaRepo   = quotaRepo;
  }

  // ── Available Balance ─────────────────────────────────────────────────────

  public BigDecimal getAvailableBalance(UUID userId, BigDecimal confirmedBalance) {
    BigDecimal activeAdvances = advanceRepo.sumActiveAdvancesByDriverId(userId);
    BigDecimal activeHolds    = holdRepo.sumActiveHoldsByUserId(userId, Instant.now());

    return confirmedBalance
      .subtract(activeAdvances)
      .subtract(activeHolds);
  }

  // ── Feature A: Advance ────────────────────────────────────────────────────

  @Transactional
  public Advance requestAdvance(UUID driverId, BigDecimal requestedAmount,
                                BigDecimal confirmedBalance) {

    BigDecimal available  = getAvailableBalance(driverId, confirmedBalance);
    BigDecimal maxAllowed = Advance.getMaxAdvance(available);

    Advance advance = new Advance(driverId, requestedAmount);

    if (requestedAmount.compareTo(maxAllowed) > 0) {
      advance.reject();
      log.info("Advance rechazado. driverId={}, solicitado={}, máximo={}",
        driverId, requestedAmount, maxAllowed);
    } else {
      advance.approve(requestedAmount);
      log.info("Advance aprobado. driverId={}, monto={}", driverId, requestedAmount);
    }

    return advanceRepo.save(advance);
  }


  @Transactional
  public void settleAdvances(UUID driverId) {
    advanceRepo.findByDriverIdAndStatus(driverId, AdvanceStatus.APPROVED)
      .forEach(advance -> {
        advance.settle();
        advanceRepo.save(advance);
        log.info("Advance liquidado. advanceId={}", advance.getId());
      });
  }

  // ── Feature B: Corporate ──────────────────────────────────────────────────


  @Transactional
  public EmployeeQuota addEmployee(UUID corporateAccountId, UUID employeeUserId,
                                   BigDecimal monthlyLimit) {
    CorporateAccount corp = corpRepo.findById(corporateAccountId)
      .orElseThrow(() -> new IllegalArgumentException(
        "Cuenta corporativa no encontrada: " + corporateAccountId
      ));

    if (!corp.isActive()) {
      throw new IllegalStateException("La cuenta corporativa está desactivada.");
    }

    EmployeeQuota quota = new EmployeeQuota(corp, employeeUserId, monthlyLimit);
    return quotaRepo.save(quota);
  }

  @Transactional
  public void authorizeCorporateTrip(UUID corporateAccountId, UUID employeeUserId,
                                     BigDecimal tripAmount) {

    CorporateAccount corp = corpRepo.findById(corporateAccountId).orElseThrow();
    EmployeeQuota quota = quotaRepo
      .findByCorporateAccountIdAndEmployeeUserId(corporateAccountId, employeeUserId)
      .orElseThrow(() -> new IllegalArgumentException("Empleado no pertenece a la empresa."));

    // Verificar cupo del empleado
    if (!quota.hasAvailable(tripAmount)) {
      throw new EmployeeQuota.QuotaExceededException(
        "Cupo mensual insuficiente para el empleado."
      );
    }

    // Verificar saldo de la empresa
    if (!corp.hasSufficientBalance(tripAmount)) {
      throw new CorporateAccount.InsufficientCorporateFundsException(
        "Saldo corporativo insuficiente."
      );
    }

    // Ambas verificaciones pasaron — consumir cupo y debitar saldo
    quota.consume(tripAmount);
    corp.debit(tripAmount);

    quotaRepo.save(quota);
    corpRepo.save(corp);

    // Alertar si el empleado está cerca del límite
    if (quota.isNearLimit()) {
      log.warn("Empleado cerca del límite mensual. userId={}, remaining={}",
        employeeUserId, quota.getRemainingQuota());
    }
  }

  // ── Feature C: Virtual Card ───────────────────────────────────────────────

  @Transactional
  public VirtualCard issueCard(UUID userId, String pomeloCardId, String lastFour) {
    VirtualCard card = new VirtualCard(userId, pomeloCardId, lastFour);
    log.info("Tarjeta virtual emitida. userId={}, lastFour={}", userId, lastFour);
    return cardRepo.save(card);
  }

  @Transactional
  public boolean authorizeCardPayment(String pomeloTxId, UUID userId,
                                      BigDecimal amount, BigDecimal availableBalance) {

    if (availableBalance.compareTo(amount) < 0) {
      log.info("Pago rechazado por fondos insuficientes. userId={}, amount={}", userId, amount);
      return false;
    }

    CardHold hold = new CardHold(userId, pomeloTxId, amount);
    holdRepo.save(hold);

    log.info("Pago autorizado. userId={}, pomeloTxId={}, amount={}", userId, pomeloTxId, amount);
    return true;
  }

  @Transactional
  public CardHold settleHold(String pomeloTxId, BigDecimal settledAmount) {
    CardHold hold = holdRepo.findByPomeloTxId(pomeloTxId)
      .orElseThrow(() -> new IllegalArgumentException(
        "Hold no encontrado para pomeloTxId: " + pomeloTxId
      ));

    hold.settle(settledAmount);
    holdRepo.save(hold);

    log.info("Hold liquidado. pomeloTxId={}, settledAmount={}", pomeloTxId, settledAmount);
    return hold;
  }

  @Transactional
  public void reverseHold(String pomeloTxId) {
    CardHold hold = holdRepo.findByPomeloTxId(pomeloTxId)
      .orElseThrow(() -> new IllegalArgumentException(
        "Hold no encontrado para pomeloTxId: " + pomeloTxId
      ));

    hold.reverse();
    holdRepo.save(hold);

    log.info("Hold revertido. pomeloTxId={}", pomeloTxId);
  }

  @Transactional
  public void expireOldHolds() {
    holdRepo.findExpiredPending(Instant.now()).forEach(hold -> {
      hold.expire();
      holdRepo.save(hold);
      log.info("Hold expirado. holdId={}", hold.getId());
    });
  }
}
