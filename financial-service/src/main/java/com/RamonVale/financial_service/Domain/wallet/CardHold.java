package com.RamonVale.financial_service.Domain.wallet;

import com.RamonVale.financial_service.Domain.ledger.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "cardHolds")
public class CardHold {
  // Tiempo de vida de un hold sin liquidar: 30 días.
  private static final long HOLD_TTL_DAYS = 30;
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  // Token de transacción de Pomelo — se usa para correlacionar
  // el webhook de autorización con el de liquidación.
  @Column(name = "pomelo_tx_id", nullable = false, unique = true)
  private String pomeloTxId;

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal amount;

  // Monto real liquidado — puede diferir del amount original
  // (ej: propina agregada en el comercio).
  @Column(name = "settled_amount", precision = 19, scale = 4)
  private BigDecimal settledAmount;

  @Column(name = "authorized_at", nullable = false, updatable = false)
  private Instant authorizedAt = Instant.now();

  // El hold expira a los 30 días si no se liquida.
  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;

  @Column(name = "settled_at")
  private Instant settledAt;

  @Column(name = "reversed_at")
  private Instant reversedAt;

  public CardHold(UUID userId, String pomeloTxId, BigDecimal amount) {
    if (pomeloTxId == null || pomeloTxId.isBlank()) {
      throw new IllegalArgumentException("El pomeloTxId no puede estar vacío.");
    }
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("El monto del hold debe ser mayor a cero.");
    }
    this.userId       = userId;
    this.pomeloTxId   = pomeloTxId;
    this.amount       = amount;
    this.authorizedAt = Instant.now();
    this.expiresAt    = this.authorizedAt.plusSeconds(HOLD_TTL_DAYS * 24 * 60 * 60);
  }

  public void settle(BigDecimal settledAmount) {
    if (isSettled()) {
      throw new IllegalStateException("Este hold ya fue liquidado.");
    }
    if (isReversed()) {
      throw new IllegalStateException("No se puede liquidar un hold revertido.");
    }
    if (isExpired()) {
      throw new IllegalStateException("No se puede liquidar un hold expirado.");
    }
    if (settledAmount == null || settledAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("El monto liquidado debe ser mayor a cero.");
    }
    this.settledAmount = settledAmount;
    this.settledAt     = Instant.now();
  }

  public void reverse() {
    if (isSettled()) {
      throw new IllegalStateException("No se puede revertir un hold ya liquidado.");
    }
    if (isReversed()) {
      throw new IllegalStateException("Este hold ya fue revertido.");
    }
    this.reversedAt = Instant.now();
  }


  public void expire() {
    if (!isExpired()) {
      throw new IllegalStateException("El hold aún no ha expirado.");
    }
    if (isSettled() || isReversed()) {
      throw new IllegalStateException("No se puede expirar un hold ya cerrado.");
    }
    this.reversedAt = Instant.now();
  }

  public boolean isExpired()   { return Instant.now().isAfter(this.expiresAt); }
  public boolean isSettled()   { return this.settledAt != null; }
  public boolean isReversed()  { return this.reversedAt != null; }
  public boolean isActive()    { return !isSettled() && !isReversed() && !isExpired(); }
}
