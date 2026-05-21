package com.RamonVale.notification_service.Domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusEvent {
  private String email;
  private String telefono;
  private String nombrePasajero;
  private String numeroDeAsiento;
  private String origen;
  private String destino;
  private String fechaHora;
}
