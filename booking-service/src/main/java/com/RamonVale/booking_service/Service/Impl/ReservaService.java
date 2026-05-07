package com.RamonVale.booking_service.Service.Impl;

import com.RamonVale.booking_service.Domain.Reserva.Reserva;
import com.RamonVale.booking_service.Dto.Reserva.ReservaRequest;
import com.RamonVale.booking_service.Dto.Reserva.ReservaResponse;
import com.RamonVale.booking_service.Repository.ReservaRepository;
import com.RamonVale.booking_service.Service.IReservaService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservaService implements IReservaService {
  private ReservaRepository reservaRepository;

  @Override
  public List<ReservaResponse> findAll() {
    return List.of();
  }

  @Override
  public ReservaResponse findByID(Long id) {
    return null;
  }

  @Override
  public ReservaResponse create(ReservaRequest reservaRequest) {
    return null;
  }

  @Override
  public void cancel(Long id) {

  }
}
