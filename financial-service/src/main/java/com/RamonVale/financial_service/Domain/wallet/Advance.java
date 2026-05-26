package com.RamonVale.financial_service.Domain.wallet;

import java.math.BigDecimal;

//genera deuda interna
//no modifica balances directamente

public class Advance {
  private Long id;
  private Long driverId;
  private BigDecimal amount;
  private BigDecimal amountApproved;
  //private AdvanceStatus status;

}
