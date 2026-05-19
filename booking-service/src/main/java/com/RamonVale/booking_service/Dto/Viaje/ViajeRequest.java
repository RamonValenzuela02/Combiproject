package com.RamonVale.booking_service.Dto.Viaje;

import com.RamonVale.booking_service.Domain.Automovil.Automovil;
import com.RamonVale.booking_service.Domain.Viaje.TipoDeViaje;
import java.time.LocalDateTime;


public record ViajeRequest(LocalDateTime fechaSalida,
                           TipoDeViaje tipoDeViaje,
                           Automovil automovil) {
}
