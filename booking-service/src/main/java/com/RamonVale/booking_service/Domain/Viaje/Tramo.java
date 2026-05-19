package com.RamonVale.booking_service.Domain.Viaje;

import lombok.Getter;

@Getter
public class Tramo {
  private Long id;
  private String ubicacionOrigen;
  private String ubicacionDestino;
  private double valor;

  public Tramo(String ubicacionOrigen, String ubicacionDestino, double valor) {
    this.ubicacionOrigen = ubicacionOrigen;
    this.ubicacionDestino = ubicacionDestino;
    this.valor = valor;
  }
}
