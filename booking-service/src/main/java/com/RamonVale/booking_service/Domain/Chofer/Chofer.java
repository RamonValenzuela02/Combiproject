package com.RamonVale.booking_service.Domain.Chofer;

import com.RamonVale.booking_service.Domain.Usuario.Usuario;
import java.time.LocalDate;

public class Chofer {
  private Long id;
  private Usuario usuario;
  private String dni;
  private LocalDate fechaDeVencimientoDeRegistro;
}
