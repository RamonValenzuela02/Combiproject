package com.RamonVale.booking_service.Domain.Viaje;

import lombok.Getter;

@Getter
public class Tramo {
  private Long id;
  private String ubicacionOrigen;
  private String ubicacionDestino;
  private boolean estaRealizada;
  private double valor;
}
