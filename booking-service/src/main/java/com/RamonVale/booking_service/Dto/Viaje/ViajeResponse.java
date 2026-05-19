package com.RamonVale.booking_service.Dto.Viaje;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Automovil.Automovil;
import com.RamonVale.booking_service.Domain.Viaje.EstadoViaje;
import com.RamonVale.booking_service.Domain.Viaje.TipoDeViaje;
import com.RamonVale.booking_service.Domain.Viaje.Tramo;
import java.time.LocalDateTime;
import java.util.List;

public record ViajeResponse (Long id,
                             EstadoViaje estadoViaje,
                             LocalDateTime fechaSalida,
                             TipoDeViaje tipoDeViaje,
                             List<Asiento>asientos,
                             List<Tramo> tramosCompletos,
                             Automovil automovil){
}
