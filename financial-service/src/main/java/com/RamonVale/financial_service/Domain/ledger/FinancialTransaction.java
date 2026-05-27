package com.RamonVale.financial_service.Domain.ledger;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table
public class FinancialTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionType type;

  @Column(name = "reference_id", nullable = false)
  private String referenceId;

  @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
  private BigDecimal totalAmount;

  // Igual al codigoReserva — previene procesar el mismo PagoConfirmado dos veces.
  @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
  private String idempotencyKey;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "transaction_id")
  private List<LedgerEntry> entries = new ArrayList<>();

  public FinancialTransaction(TransactionType type,
                              String referenceId,
                              BigDecimal totalAmount,
                              String idempotencyKey) {
    this.type           = type;
    this.referenceId    = referenceId;
    this.totalAmount    = totalAmount;
    this.idempotencyKey = idempotencyKey;
    this.createdAt      = Instant.now();
  }

  public void addEntry(LedgerEntry entry) {
    this.entries.add(entry);
  }

  public boolean validateBalance() {
    BigDecimal totalDebits  = entries.stream()
      .filter(LedgerEntry::isDebit)
      .map(LedgerEntry::getAmount)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalCredits = entries.stream()
      .filter(LedgerEntry::isCredit)
      .map(LedgerEntry::getAmount)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    return totalDebits.compareTo(totalCredits) == 0;
  }

  public boolean hasSameIdempotencyKey(String key) {
    return this.idempotencyKey.equals(key);
  }

}
