package com.RamonVale.financial_service.Domain.ledger;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LedgerEntry {
  private Account account;
  private BigDecimal valor;
  private EntryType type;

  public LedgerEntry(Account cuenta,
                     BigDecimal valor,
                     EntryType type) {
    this.account = cuenta;
    this.valor = valor;
    this.type = type;
  }
}
