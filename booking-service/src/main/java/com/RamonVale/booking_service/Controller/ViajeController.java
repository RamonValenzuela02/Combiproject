package com.RamonVale.booking_service.Controller;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Viaje.Viaje;
import com.RamonVale.booking_service.Dto.Asiento.AsientoResponse;
import com.RamonVale.booking_service.Dto.Viaje.ViajeRequest;
import com.RamonVale.booking_service.Dto.Viaje.ViajeResponse;
import com.RamonVale.booking_service.Service.IViajeService;
import java.util.List;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trips")
public class ViajeController {
  private IViajeService viajeService;

//  @GetMapping
//  public Page<ViajeResponse> getViajes(@PageableDefault(page=0, size=10) Pageable pageable) {
//    return viajeService.getViajes(pageable);
//  }

  @GetMapping("/{id}")
  public List<ViajeResponse> getViajesPorId (@PathVariable Long id) {
    return viajeService.getViajeById(id);
  }

  @GetMapping("/{id}/seats")
  public List<AsientoResponse> getAsientos(@PathVariable Long id, @RequestParam String origen, @RequestParam String destino) {
    return viajeService.getAsientos(id);
  }

  @PostMapping("/new")
  public ViajeResponse crearViaje(@RequestBody ViajeRequest viaje) {
    return viajeService.create(viaje);
  }

  @PutMapping("/{id}/start")
  public ViajeResponse startViaje(@PathVariable Long id) {
    return viajeService.arrancarViaje(id);
  }

  @PutMapping("/{id}/end")
  public ViajeResponse endViaje(@PathVariable Long id) {
    return viajeService.terminarViaje(id);
  }

  @PutMapping("/{id}/cancel")
  public ViajeResponse cancelViaje(@PathVariable Long id) {
    return viajeService.cancelarViaje(id);
  }


}
