package com.RamonVale.booking_service.Domain.Viaje;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoDeViaje {
  private Long id;
  private String destino;
  private String origen;
  private List<Tramo> tramosCompletos;
}
