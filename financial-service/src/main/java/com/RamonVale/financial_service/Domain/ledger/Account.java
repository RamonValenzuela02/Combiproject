package com.RamonVale.financial_service.Domain.ledger;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
  private Long id;
  private Long idUsuario;
  private BigDecimal balance;
  private String nombre;
  //private AcountType type;

  public Account(String nombre) {
    this.nombre = nombre;
  }

}
