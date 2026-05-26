package com.RamonVale.financial_service.Domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FinancialTransaction {
  private Long id;
  //private String idempotenciasKey; idempotencia key asi si no se hace dos veces
  private List<LedgerEntry> entradas;
  private LocalDateTime fecha;

  public FinancialTransaction(List<LedgerEntry> entradas) {
    validarBalance(entradas);
    this.entradas = entradas;
  }

  private void validarBalance(List<LedgerEntry> entradas) {
    BigDecimal total = entradas.stream()
      .map(LedgerEntry::getValor)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (total.compareTo(BigDecimal.ZERO) != 0) {
      throw new IllegalArgumentException(
        "La transaccion no esta correctamente balanceada"
      );
    }
  }
}
