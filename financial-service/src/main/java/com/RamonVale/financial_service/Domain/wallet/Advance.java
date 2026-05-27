package com.RamonVale.financial_service.Domain.wallet;

import com.RamonVale.financial_service.Domain.ledger.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "advances")
public class Advance {
  private static final BigDecimal MAX_ADVANCE_PERCENT = new BigDecimal("0.70");
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "driver_id", nullable = false)
  private UUID driverId;

  @Column(name = "requested_amount", nullable = false, precision = 19, scale = 4)
  private BigDecimal requestedAmount;

  @Column(name = "approved_amount", precision = 19, scale = 4)
  private BigDecimal approvedAmount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AdvanceStatus status = AdvanceStatus.PENDING;

  @Column(name = "requested_at", nullable = false, updatable = false)
  private Instant requestedAt = Instant.now();

  @Column(name = "settled_at")
  private Instant settledAt;

  public Advance(UUID driverId, BigDecimal requestedAmount) {
    if (requestedAmount == null || requestedAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException(
        "El monto solicitado debe ser mayor a cero. Recibido: " + requestedAmount
      );
    }
    this.driverId        = driverId;
    this.requestedAmount = requestedAmount;
    this.status          = AdvanceStatus.PENDING;
    this.requestedAt     = Instant.now();
  }


  public void approve(BigDecimal approvedAmount) {
    if (this.status != AdvanceStatus.PENDING) {
      throw new IllegalStateException(
        "Solo se puede aprobar un adelanto PENDING. Estado actual: " + this.status
      );
    }
    if (approvedAmount == null || approvedAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("El monto aprobado debe ser mayor a cero.");
    }
    this.approvedAmount = approvedAmount;
    this.status         = AdvanceStatus.APPROVED;
  }

  public void reject() {
    if (this.status != AdvanceStatus.PENDING) {
      throw new IllegalStateException(
        "Solo se puede rechazar un adelanto PENDING. Estado actual: " + this.status
      );
    }
    this.status = AdvanceStatus.REJECTED;
  }

  public void settle() {
    if (this.status != AdvanceStatus.APPROVED) {
      throw new IllegalStateException(
        "Solo se puede liquidar un adelanto APPROVED. Estado actual: " + this.status
      );
    }
    this.status    = AdvanceStatus.SETTLED;
    this.settledAt = Instant.now();
  }

  public static BigDecimal getMaxAdvance(BigDecimal availableBalance) {
    if (availableBalance == null || availableBalance.compareTo(BigDecimal.ZERO) <= 0) {
      return BigDecimal.ZERO;
    }
    return availableBalance.multiply(MAX_ADVANCE_PERCENT)
      .setScale(4, RoundingMode.HALF_EVEN);
  }

  public boolean isPending()  { return this.status == AdvanceStatus.PENDING; }
  public boolean isApproved() { return this.status == AdvanceStatus.APPROVED; }
  public boolean isSettled()  { return this.status == AdvanceStatus.SETTLED; }
}
