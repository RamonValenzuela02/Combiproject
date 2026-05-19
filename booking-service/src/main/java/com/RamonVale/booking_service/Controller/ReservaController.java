package com.RamonVale.booking_service.Controller;

import com.RamonVale.booking_service.Dto.Reserva.ReservaRequest;
import com.RamonVale.booking_service.Dto.Reserva.ReservaResponse;
import com.RamonVale.booking_service.Service.IReservaService;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/booking")
public class ReservaController {
  @Autowired
  private IReservaService reservaService;

//  @GetMapping("/me")
//  public Page<ReservaResponse> getReservas(@PageableDefault(page=0, size=10) Pageable pageable) {
//    return reservaService.findAll(pageable);
//  }

  @GetMapping("/{id}")
  public ReservaResponse getReserva(@PathVariable Long id) {
    return reservaService.findByID(id);
  }

  @PostMapping("/new")
  public ReservaResponse create(@RequestBody ReservaRequest reservaRequest) {
    return reservaService.create(reservaRequest);
  }

  @PutMapping("/{id}/cancel")
  public void cancel(@PathVariable Long id) {
    reservaService.cancel(id);
  }



}
