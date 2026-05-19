package com.RamonVale.booking_service.Domain.Viaje;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoDeViaje {
  private Long id;
  private String destino;
  private String origen;
  private List<Tramo> tramosCompletos;

  public TipoDeViaje(String destino, String origen, List<Tramo> tramosCompletos) {
    this.destino = destino;
    this.origen = origen;
    this.tramosCompletos = tramosCompletos;
  }

  public double getPrecioFinal() {
    return tramosCompletos
      .stream()
      .mapToDouble(Tramo::getValor)
      .sum();
  }
}
