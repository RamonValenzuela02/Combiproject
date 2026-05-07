package com.RamonVale.booking_service.Service.Impl;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Viaje.Viaje;
import com.RamonVale.booking_service.Repository.ViajeRepository;
import com.RamonVale.booking_service.Service.IViajeService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ViajeService implements IViajeService {
  private ViajeRepository viajeRepository;

  @Override
  public List<Viaje> getViajes() {
    return viajeRepository.findAll();
  }

  @Override
  public List<Viaje> getViajeById(Long id) {
    return viajeRepository.findById(id);
  }

  @Override
  public List<Asiento> getAsientos(Long id) {
    return viajeRepository.findById(id).getAsientos();
  }

}
