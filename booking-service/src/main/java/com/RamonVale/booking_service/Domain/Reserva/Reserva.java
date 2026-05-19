package com.RamonVale.booking_service.Domain.Reserva;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Pasajero.Pasajero;
import com.RamonVale.booking_service.Domain.Viaje.Tramo;
import com.RamonVale.booking_service.Domain.Usuario.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reserva {
  private Long id;
  private String codigo;
  private Asiento asiento;
  private List<Tramo> tramo;
  private Pasajero pasajero;
  private EstadoDeReserva estado;
  private LocalDateTime inicioDeReserva;

  public Reserva(Long id, Asiento asiento, List<Tramo> tramo, Pasajero pasajero) {
    this.id = id;
    this.asiento = asiento;
    this.tramo = tramo;
    this.pasajero = pasajero;
  }

  public void reservar() {

    asiento.esReservado();
    this.estado = EstadoDeReserva.PENDENTE;
    this.inicioDeReserva = LocalDateTime.now();
  }

  public void cancelar() {
    if (this.estado == EstadoDeReserva.PENDENTE || this.estado == EstadoDeReserva.CONFIRMADO) {
      asiento.estaLibre();
      this.estado = EstadoDeReserva.CANCELADA;
    }else{
      throw new RuntimeException("no se puede cancelar una reserva que ya esta cancelada");
    }

  }

  public void confirmar() {
    if(this.estado == EstadoDeReserva.PENDENTE) {
      asiento.estaConfirmada();
      this.estado = EstadoDeReserva.CONFIRMADO;
    }else{
      throw new RuntimeException("no se puede confirmar sin antes reservar, o si ya esta confirmada o cancelada");
    }
  }

  public void cancelarPorTimeOut() {
    if (cumpleConTimeOut()) {
      estado = EstadoDeReserva.CACELADA_POR_TIME_OUT;
      asiento.estaLibre();
    }
  }

  private boolean cumpleConTimeOut() {
    //todo falta logica
    return false;
  }


}
