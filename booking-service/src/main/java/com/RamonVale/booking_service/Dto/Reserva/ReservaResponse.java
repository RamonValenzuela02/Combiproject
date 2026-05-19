package com.RamonVale.booking_service.Dto.Reserva;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Pasajero.Pasajero;
import com.RamonVale.booking_service.Domain.Reserva.EstadoDeReserva;
import com.RamonVale.booking_service.Domain.Viaje.Tramo;
import com.RamonVale.booking_service.Dto.Asiento.AsientoResponse;
import java.time.LocalDateTime;
import java.util.List;

public record ReservaResponse(String codigo,
                              Long asiento,
                              List<Tramo> tramo,
                              Long pasajero,
                              EstadoDeReserva estado,
                              LocalDateTime inicioDeReserva) {
}
