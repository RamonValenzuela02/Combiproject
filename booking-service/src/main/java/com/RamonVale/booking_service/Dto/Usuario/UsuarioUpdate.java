package com.RamonVale.booking_service.Dto.Usuario;

import java.time.LocalDate;

public record UsuarioUpdate(String nombre,
                            String apellido,
                            String email,
                            String telefono,
                            LocalDate fechaNacimiento) {
}
