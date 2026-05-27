package com.RamonVale.financial_service.Domain.report;

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
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "ReconciliationReports")
public class ReconciliationReport {
  // Umbral de drift aceptable: diferencias <= 1 centavo se ignoran (redondeo).
  private static final BigDecimal DRIFT_THRESHOLD = new BigDecimal("0.01");
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private LocalDate date;

  @Column(name = "total_debits", nullable = false, precision = 19, scale = 4)
  private BigDecimal totalDebits;

  @Column(name = "total_credits", nullable = false, precision = 19, scale = 4)
  private BigDecimal totalCredits;

  // Diferencia absoluta: abs(totalDebits - totalCredits).
  // En un sistema sano, drift == 0. Cualquier valor > 0 indica problema.
  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal drift;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status;

  @Column(name = "generated_at", nullable = false, updatable = false)
  private Instant generatedAt = Instant.now();

  public ReconciliationReport(
    LocalDate date,
    BigDecimal totalDebits,
    BigDecimal totalCredits) {
    this.date         = date;
    this.totalDebits  = totalDebits;
    this.totalCredits = totalCredits;
    this.drift        = totalDebits.subtract(totalCredits).abs()
      .setScale(4, RoundingMode.HALF_EVEN);
    this.status       = this.hasDriftAbove(DRIFT_THRESHOLD)
      ? Status.ALERT
      : Status.OK;
  }


  public boolean isBalanced() {
    return this.status == Status.OK;
  }

  public BigDecimal getDrift() {
    return drift;
  }

  public boolean hasDriftAbove(BigDecimal threshold) {
    return this.drift.compareTo(threshold) > 0;
  }
}
