package com.RamonVale.booking_service.Dto.Reserva;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Pasajero.Pasajero;
import com.RamonVale.booking_service.Domain.Viaje.Tramo;

public record ReservaRequest(Asiento asiento,
                             Tramo tramo,
                             Pasajero pasajero) {
}
