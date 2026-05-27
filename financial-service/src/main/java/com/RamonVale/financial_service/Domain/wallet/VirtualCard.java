package com.RamonVale.financial_service.Domain.wallet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "virtualCards")
public class VirtualCard {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "pomelo_card_id", nullable = false, unique = true)
  private String pomeloCardId;

  @Column(name = "last_four", nullable = false, length = 4)
  private String lastFour;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CardStatus status = CardStatus.ACTIVE;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  protected VirtualCard() {}

  public VirtualCard(UUID userId, String pomeloCardId, String lastFour) {
    if (pomeloCardId == null || pomeloCardId.isBlank()) {
      throw new IllegalArgumentException("El pomeloCardId no puede estar vacío.");
    }
    if (lastFour == null || lastFour.length() != 4) {
      throw new IllegalArgumentException("lastFour debe tener exactamente 4 dígitos.");
    }
    this.userId       = userId;
    this.pomeloCardId = pomeloCardId;
    this.lastFour     = lastFour;
    this.status       = CardStatus.ACTIVE;
    this.createdAt    = Instant.now();
  }

  /**
   * Bloquea la tarjeta localmente.
   * El WalletService debe también llamar a la API de Pomelo para bloquearla allá.
   */
  public void block() {
    if (this.status == CardStatus.CANCELLED) {
      throw new IllegalStateException("No se puede bloquear una tarjeta cancelada.");
    }
    this.status = CardStatus.BLOCKED;
  }

  /**
   * Desbloquea la tarjeta localmente.
   * El WalletService debe también llamar a la API de Pomelo.
   */
  public void unblock() {
    if (this.status != CardStatus.BLOCKED) {
      throw new IllegalStateException(
        "Solo se puede desbloquear una tarjeta BLOCKED. Estado actual: " + this.status
      );
    }
    this.status = CardStatus.ACTIVE;
  }

  /**
   * Cancela la tarjeta permanentemente.
   * Una tarjeta cancelada no puede volver a activarse.
   */
  public void cancel() {
    this.status = CardStatus.CANCELLED;
  }

  public boolean isActive()    { return this.status == CardStatus.ACTIVE; }
  public boolean isBlocked()   { return this.status == CardStatus.BLOCKED; }
  public boolean isCancelled() { return this.status == CardStatus.CANCELLED; }
}
