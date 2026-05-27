package com.RamonVale.financial_service.Domain.ledger;

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
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "LedgerEntries")
public class LedgerEntry {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  // Agrupa las 3 entradas del mismo viaje (o reversal).
//  @Column(name = "transaction_id", nullable = false)
//  private UUID transactionId;

  // A qué cuenta afecta esta entrada.
  @Column(name = "account_id", nullable = false)
  private UUID accountId;

  @Enumerated(EnumType.STRING)
  @Column(name = "entry_type", nullable = false, length = 10)
  private EntryType entryType;

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal amount;

  @Column(name = "balance_after", nullable = false, precision = 19, scale = 4)
  private BigDecimal balanceAfter;

  @Column(length = 200)
  private String description;

  // Previene que el mismo evento de KAFKA genere entradas duplicadas.
  @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
  private String idempotencyKey;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();



  public LedgerEntry(//UUID transactionId,
                     UUID accountId,
                     EntryType entryType,
                     BigDecimal amount,
                     BigDecimal balanceAfter,
                     String idempotencyKey,
                     String description) {
    //this.transactionId  = transactionId;
    this.accountId      = accountId;
    this.entryType      = entryType;
    this.amount         = amount;
    this.balanceAfter   = balanceAfter;
    this.idempotencyKey = idempotencyKey;
    this.description    = description;
    this.createdAt      = Instant.now();
  }

  public boolean isDebit() {
    return this.entryType == EntryType.DEBIT;
  }

  public boolean isCredit() {
    return this.entryType == EntryType.CREDIT;
  }
}
