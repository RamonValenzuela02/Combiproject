package com.RamonVale.financial_service.Dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record ReconciliationAlertEvent(
  LocalDate date,
  BigDecimal totalDebits,
  BigDecimal totalCredits,
  BigDecimal drift,
  Instant timestamp
) {
}
