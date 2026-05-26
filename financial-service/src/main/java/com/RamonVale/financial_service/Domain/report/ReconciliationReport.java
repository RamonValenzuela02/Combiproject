package com.RamonVale.financial_service.Domain.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReconciliationReport {
  private Long id;
  private LocalDateTime fecha;
  private Status status;
  private BigDecimal totalDebito;
  private BigDecimal totalCredito;
}
