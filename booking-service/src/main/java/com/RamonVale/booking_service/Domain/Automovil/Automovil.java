package com.RamonVale.booking_service.Domain.Automovil;

import com.RamonVale.booking_service.Domain.Chofer.Chofer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Automovil {
  private Long id;
  private String patente;
  private Chofer chofer;
  private int cantidadDeAsientos;

  public Automovil(String patente, Chofer chofer, int cantidadDeAsientos) {
    this.patente = patente;
    this.chofer = chofer;
    this.cantidadDeAsientos = cantidadDeAsientos;
  }

}
