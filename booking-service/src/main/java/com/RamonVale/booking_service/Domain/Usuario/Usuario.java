package com.RamonVale.booking_service.Domain.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Usuario {
  private Long id;
  private String nombre;
  private String apellido;
  private String email;
  private String telefono;
  private LocalDate fechaNacimiento;
  private LocalDateTime ultimoLogin;
  private int edad;
}
