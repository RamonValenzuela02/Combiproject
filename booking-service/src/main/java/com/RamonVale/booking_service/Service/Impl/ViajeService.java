package com.RamonVale.booking_service.Service.Impl;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Viaje.Viaje;
import com.RamonVale.booking_service.Dto.Asiento.AsientoResponse;
import com.RamonVale.booking_service.Dto.Viaje.ViajeRequest;
import com.RamonVale.booking_service.Dto.Viaje.ViajeResponse;
import com.RamonVale.booking_service.Repository.ViajeRepository;
import com.RamonVale.booking_service.Service.IViajeService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ViajeService implements IViajeService {
  private ViajeRepository viajeRepository;

  @Override
  public List<ViajeResponse> getViajes() {
    return null;
  }

  @Override
  public List<ViajeResponse> getViajeById(Long id) {
    return null;
  }

  @Override
  public List<AsientoResponse> getAsientos(Long id) {
    return null;
  }

  @Override
  public ViajeResponse create(ViajeRequest viaje) {
    return null;
  }

  @Override
  public ViajeResponse arrancarViaje(Long id) {
    return null;
  }

  @Override
  public ViajeResponse terminarViaje(Long id) {
    return null;
  }

  @Override
  public ViajeResponse cancelarViaje(Long id) {
    return null;
  }

}
