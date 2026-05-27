package com.RamonVale.financial_service.Domain.wallet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Entity
@Table(name = "corporateAccounts")
public class CorporateAccount {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "company_name", nullable = false)
  private String companyName;

  @Column(name = "total_balance", nullable = false, precision = 19, scale = 4)
  private BigDecimal totalBalance = BigDecimal.ZERO;

  @Column(name = "monthly_limit", nullable = false, precision = 19, scale = 4)
  private BigDecimal monthlyLimit;

  @Column(nullable = false)
  private boolean active = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  protected CorporateAccount() {}

  public CorporateAccount(String companyName, BigDecimal monthlyLimit) {
    if (companyName == null || companyName.isBlank()) {
      throw new IllegalArgumentException("El nombre de la empresa no puede estar vacío.");
    }
    if (monthlyLimit == null || monthlyLimit.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("El límite mensual debe ser mayor a cero.");
    }
    this.companyName  = companyName;
    this.monthlyLimit = monthlyLimit;
    this.createdAt    = Instant.now();
  }

  public void topUp(BigDecimal amount) {
    validateActive();
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("El monto de recarga debe ser mayor a cero.");
    }
    this.totalBalance = this.totalBalance.add(amount);
  }

  public void debit(BigDecimal amount) {
    validateActive();
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("El monto a debitar debe ser mayor a cero.");
    }
    if (this.totalBalance.compareTo(amount) < 0) {
      throw new InsufficientCorporateFundsException(
        "Saldo corporativo insuficiente. Disponible: %s, Requerido: %s"
          .formatted(this.totalBalance, amount)
      );
    }
    this.totalBalance = this.totalBalance.subtract(amount);
  }

  public boolean hasSufficientBalance(BigDecimal amount) {
    return this.active && this.totalBalance.compareTo(amount) >= 0;
  }

  public void deactivate() {
    this.active = false;
  }

  public boolean isActive() { return active; }

  private void validateActive() {
    if (!this.active) {
      throw new IllegalStateException("La cuenta corporativa está desactivada.");
    }
  }

  // ── Excepción de dominio ──────────────────────────────────────────────────

  public static class InsufficientCorporateFundsException extends RuntimeException {
    public InsufficientCorporateFundsException(String message) {
      super(message);
    }
  }
}
