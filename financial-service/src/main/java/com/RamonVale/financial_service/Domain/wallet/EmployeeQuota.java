package com.RamonVale.financial_service.Domain.wallet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "employeesQuota")
public class EmployeeQuota {
 // Umbral para alertar cuando el empleado está cerca del límite mensual.
  private static final BigDecimal NEAR_LIMIT_THRESHOLD = new BigDecimal("0.90");
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "corporate_account_id", nullable = false)
  private CorporateAccount corporateAccountId;

  @Column(name = "employee_user_id", nullable = false)
  private UUID employeeUserId;

  @Column(name = "monthly_limit", nullable = false, precision = 19, scale = 4)
  private BigDecimal monthlyLimit;

  @Column(name = "used_this_month", nullable = false, precision = 19, scale = 4)
  private BigDecimal usedThisMonth = BigDecimal.ZERO;

  // Día del mes en que se resetea el cupo. Default: 1 (primer día del mes).
  @Column(name = "reset_day", nullable = false)
  private int resetDay = 1;

  protected EmployeeQuota() {}

  public EmployeeQuota(CorporateAccount corporateAccountId, UUID employeeUserId, BigDecimal monthlyLimit) {
    if (monthlyLimit == null || monthlyLimit.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("El límite mensual debe ser mayor a cero.");
    }
    this.corporateAccountId = corporateAccountId;
    this.employeeUserId     = employeeUserId;
    this.monthlyLimit       = monthlyLimit;
    this.usedThisMonth      = BigDecimal.ZERO;
  }


  public boolean hasAvailable(BigDecimal amount) {
    return getRemainingQuota().compareTo(amount) >= 0;
  }


  public void consume(BigDecimal amount) {
    if (!hasAvailable(amount)) {
      throw new QuotaExceededException(
        "Cupo mensual insuficiente. Disponible: %s, Requerido: %s"
          .formatted(getRemainingQuota(), amount)
      );
    }
    this.usedThisMonth = this.usedThisMonth.add(amount);
  }

  public void resetMonthly() {
    this.usedThisMonth = BigDecimal.ZERO;
  }

  public BigDecimal getRemainingQuota() {
    return this.monthlyLimit.subtract(this.usedThisMonth);
  }

  public boolean isNearLimit(BigDecimal threshold) {
    if (this.monthlyLimit.compareTo(BigDecimal.ZERO) == 0) return false;
    BigDecimal usagePercent = this.usedThisMonth.divide(
      this.monthlyLimit, 4, java.math.RoundingMode.HALF_EVEN
    );
    return usagePercent.compareTo(threshold) >= 0;
  }


  public boolean isNearLimit() {
    return isNearLimit(NEAR_LIMIT_THRESHOLD);
  }


  // ── Excepción de dominio ──────────────────────────────────────────────────
  public static class QuotaExceededException extends RuntimeException {
    public QuotaExceededException(String message) {
      super(message);
    }
  }
}
