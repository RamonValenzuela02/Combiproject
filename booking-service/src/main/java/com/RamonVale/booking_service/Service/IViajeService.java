package com.RamonVale.booking_service.Service;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Viaje.Viaje;
import com.RamonVale.booking_service.Dto.Asiento.AsientoResponse;
import com.RamonVale.booking_service.Dto.Viaje.ViajeRequest;
import com.RamonVale.booking_service.Dto.Viaje.ViajeResponse;
import java.util.List;


public interface IViajeService {
  List<ViajeResponse> getViajes();
  List<ViajeResponse> getViajeById(Long id);
  List<AsientoResponse> getAsientos(Long id);
  ViajeResponse create(ViajeRequest viaje);
  ViajeResponse arrancarViaje(Long id);
  ViajeResponse terminarViaje(Long id);

  ViajeResponse cancelarViaje(Long id);
}
