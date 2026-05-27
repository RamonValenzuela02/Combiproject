package com.RamonVale.financial_service.Service;

import com.RamonVale.financial_service.Domain.ledger.Account;
import com.RamonVale.financial_service.Domain.ledger.AccountType;
import com.RamonVale.financial_service.Domain.ledger.EntryType;
import com.RamonVale.financial_service.Domain.ledger.FinancialTransaction;
import com.RamonVale.financial_service.Domain.ledger.LedgerEntry;
import com.RamonVale.financial_service.Domain.ledger.TransactionType;
import com.RamonVale.financial_service.Repository.AccountRepository;
import com.RamonVale.financial_service.Repository.FinancialTransactionRepository;
import io.github.resilience4j.retry.annotation.Retry;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;



@Service
public class LedgerService {
  private static final Logger log = LoggerFactory.getLogger(LedgerService.class);
  private static final BigDecimal PLATFORM_FEE_PERCENT = new BigDecimal("0.50");
  private static final BigDecimal DRIVER_SHARE_PERCENT = new BigDecimal("0.50");
  private final AccountRepository accountRepo;
  private final FinancialTransactionRepository txRepo;

  public LedgerService(AccountRepository accountRepo,
                       FinancialTransactionRepository txRepo) {
    this.accountRepo = accountRepo;
    this.txRepo      = txRepo;
  }

  @Retry(name = "ledgerRetry", fallbackMethod = "handleRetryFailure")
  @Transactional
  public FinancialTransaction processTrip(
    String reservaId,
    String passengerId,
    String driverId,
    BigDecimal totalAmount) {

    // 1. Idempotencia
    if (txRepo.existsByIdempotencyKey(reservaId)) {
      log.info("Evento duplicado ignorado. reservaId={}", reservaId);
      return txRepo.findByIdempotencyKey(reservaId).orElseThrow();
    }

    // 2. Calcular montos
    BigDecimal platformFee   = totalAmount.multiply(PLATFORM_FEE_PERCENT)
      .setScale(4, RoundingMode.HALF_EVEN);
    BigDecimal driverEarning = totalAmount.subtract(platformFee);

    // 3. Obtener o crear cuentas
    Account passengerAccount = getOrCreateAccount(passengerId, AccountType.PASSENGER);
    Account driverAccount    = getOrCreateAccount(driverId,    AccountType.DRIVER);
    Account feeAccount       = getOrCreateAccount("platform",  AccountType.BUSINESS_OWNER);

    // 4. Crear la transacción
    FinancialTransaction tx = new FinancialTransaction(
      TransactionType.TRIP_COMPLETED,
      reservaId,
      totalAmount,
      reservaId
    );

    // 5. Aplicar movimientos y agregar entries a la transacción
    passengerAccount.debit(totalAmount);
    tx.addEntry(new LedgerEntry(
      passengerAccount.getId(), EntryType.DEBIT,
      totalAmount, passengerAccount.getBalance(),
      "TRIP:%s:DEBIT:PASSENGER".formatted(reservaId),
      "Pago viaje %s".formatted(reservaId)
    ));

    driverAccount.credit(driverEarning);
    tx.addEntry(new LedgerEntry(
      driverAccount.getId(), EntryType.CREDIT,
      driverEarning, driverAccount.getBalance(),
      "TRIP:%s:CREDIT:DRIVER".formatted(reservaId),
      "Ganancia chofer viaje %s".formatted(reservaId)
    ));

    feeAccount.credit(platformFee);
    tx.addEntry(new LedgerEntry(
      feeAccount.getId(), EntryType.CREDIT,
      platformFee, feeAccount.getBalance(),
      "TRIP:%s:CREDIT:PLATFORM".formatted(reservaId),
      "Comisión plataforma viaje %s".formatted(reservaId)
    ));

    // 6. Validar antes de guardar
    if (!tx.validateBalance()) {
      throw new IllegalStateException(
        "El ledger no cuadra para reservaId=%s.".formatted(reservaId)
      );
    }

    // 7. Un solo save — guarda la tx Y las 3 entries por cascade
    accountRepo.save(passengerAccount);
    accountRepo.save(driverAccount);
    accountRepo.save(feeAccount);
    txRepo.save(tx);

    log.info("Viaje procesado. reservaId={}, total={}, fee={}, driver={}",
      reservaId, totalAmount, platformFee, driverEarning);

    return tx;
  }

  @Retry(name = "ledgerRetry", fallbackMethod = "handleRetryFailure")
  @Transactional
  public FinancialTransaction reverseTrip(String reservaId) {

    // Si no existe la transacción original, el pago no fue procesado → ignorar.
    FinancialTransaction original = txRepo.findByReferenceId(reservaId)
      .orElse(null);

    if (original == null) {
      log.info("No existe transacción para reservaId={}. Reversal ignorado.", reservaId);
      return null;
    }

    // Idempotencia: si ya existe el reversal, ignorarlo.
    String reversalKey = "REVERSAL:" + reservaId;
    if (txRepo.existsByIdempotencyKey(reversalKey)) {
      log.info("Reversal duplicado ignorado. reservaId={}", reservaId);
      return txRepo.findByIdempotencyKey(reversalKey).orElseThrow();
    }

    BigDecimal totalAmount  = original.getTotalAmount();
    BigDecimal platformFee  = totalAmount.multiply(PLATFORM_FEE_PERCENT)
      .setScale(4, RoundingMode.HALF_EVEN);
    BigDecimal driverEarning = totalAmount.multiply(DRIVER_SHARE_PERCENT)
      .setScale(4, RoundingMode.HALF_EVEN);

    FinancialTransaction reversal = new FinancialTransaction(
      TransactionType.REFUND,
      "REVERSAL:" + reservaId,
      original.getTotalAmount(),
      "REVERSAL:" + reservaId
    );
    txRepo.save(reversal);

    log.info("Reversal completado para reservaId={}", reservaId);
    return reversal;
  }

  @Transactional(readOnly = true)
  public BigDecimal getBalance(String userId, boolean isDriver) {
    AccountType type = isDriver
      ? AccountType.DRIVER
      : AccountType.PASSENGER;

    return accountRepo.findByOwnerIdAndAccountType(userId, type)
      .map(Account::getAvailableBalance)
      .orElse(BigDecimal.ZERO);
  }

  private Account getOrCreateAccount(String ownerId, AccountType type) {
    return accountRepo.findByOwnerIdAndAccountType(ownerId, type)
      .orElseGet(() -> accountRepo.save(new Account(ownerId, type)));
  }

  private FinancialTransaction handleRetryFailure( String reservaId,
                                                   String passengerId,
                                                   String driverId,
                                                   BigDecimal totalAmount,
                                                   Throwable t) {
    log.error("Se agotaron los reintentos para reservaId={}", reservaId, t);
    throw new RuntimeException("No se pudo procesar el viaje después de varios intentos", t);
  }
}
