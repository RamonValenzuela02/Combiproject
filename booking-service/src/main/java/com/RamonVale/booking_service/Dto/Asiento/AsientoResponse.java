package com.RamonVale.booking_service.Dto.Asiento;

import com.RamonVale.booking_service.Domain.Asiento.EstadoAsiento;

public record AsientoResponse(Long id,
                              EstadoAsiento estado) {
}
