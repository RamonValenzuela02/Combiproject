package com.RamonVale.booking_service.Service;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Viaje.Viaje;
import java.util.List;


public interface IViajeService {
  List<Viaje> getViajes();
  List<Viaje> getViajeById(Long id);
  List<Asiento> getAsientos(Long id);
}
