package com.RamonVale.financial_service.Domain.ledger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@NoArgsConstructor
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_type", nullable = false)
  private AccountType accountType;

  @Column(name = "owner_id", nullable = false)
  private String ownerId;

  @Column(nullable = false, length = 3)
  private String currency = "ARS";

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal balance = BigDecimal.ZERO;

  @Version
  private Long version;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();


  public Account(String ownerId, AccountType accountType) {
    this.ownerId      = ownerId;
    this.accountType  = accountType;
    this.balance      = BigDecimal.ZERO;
    this.createdAt    = Instant.now();
  }

  public void credit(BigDecimal amount) {
    validateAmount(amount);
    this.balance = this.balance.add(amount);
  }

  public void debit(BigDecimal amount) {
    validateAmount(amount);
    if (this.balance.compareTo(amount) < 0) {
      throw new InsufficientFundsException(
        "Balance insuficiente en cuenta %s. Disponible: %s, Requerido: %s"
          .formatted(this.id, this.balance, amount)
      );
    }
    this.balance = this.balance.subtract(amount);
  }


  public BigDecimal getAvailableBalance() {
    return this.balance;
  }

  private void validateAmount(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException(
        "El monto debe ser mayor a cero. Recibido: " + amount
      );
    }
  }

  //exeption despues poner en la carpeta de exceptions
  public static class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
      super(message);
    }
  }

}
