package com.RamonVale.booking_service.Domain.Asiento;

import com.RamonVale.booking_service.Domain.Viaje.Viaje;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Asiento {
  private Long id;
  private Viaje viaje;
  private EstadoAsiento estado;

  public Asiento(Viaje viaje) {
    this.id = 1L;
    this.viaje = viaje;
    this.estado = EstadoAsiento.LIBRE;
  }

  public Boolean esLibre() {
    return estado == EstadoAsiento.LIBRE;
  }

  public void esReservado() {
    estado = EstadoAsiento.RESERVADO;
  }

  public void estaLibre() {
    estado = EstadoAsiento.LIBRE;
  }

  public void estaConfirmada() {
    estado = EstadoAsiento.OCUPADO;
  }
}
