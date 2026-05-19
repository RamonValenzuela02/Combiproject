package com.RamonVale.booking_service.Domain.Pasajero;

import com.RamonVale.booking_service.Domain.Usuario.Usuario;
import java.time.LocalDateTime;

public class Pasajero {
  private Long id;
  private Usuario usuario;
  private LocalDateTime ultimoLogin;

  public Pasajero(Usuario usuario) {
    this.usuario = usuario;
  }
}
