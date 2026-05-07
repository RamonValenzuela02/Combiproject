package com.RamonVale.booking_service.Service;

import com.RamonVale.booking_service.Dto.Reserva.ReservaRequest;
import com.RamonVale.booking_service.Dto.Reserva.ReservaResponse;
import java.util.List;

public interface IReservaService {
  List<ReservaResponse> findAll();
  ReservaResponse findByID(Long id);
  ReservaResponse create(ReservaRequest reservaRequest);
  void cancel(Long id);

}
