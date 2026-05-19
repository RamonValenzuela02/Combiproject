package com.RamonVale.booking_service.Dto.Usuario;

import java.time.LocalDate;

public record UsuarioResponse(Long id,
                              String nombre,
                              String apellido,
                              String email,
                              String telefono,
                              LocalDate fechaNacimiento) {
}
