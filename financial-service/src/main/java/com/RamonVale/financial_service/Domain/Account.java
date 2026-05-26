package com.RamonVale.financial_service.Domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
  private Long id;
  private Long idUsuario;
  //private Balance balance;
  private String nombre;
  //private AcountType type;

  public Account(String nombre) {
    this.nombre = nombre;
    //this.type = type;
  }

}
