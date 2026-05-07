package com.RamonVale.booking_service.Domain.Reserva;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Viaje.Tramo;
import com.RamonVale.booking_service.Domain.Usuario.Usuario;
import java.time.LocalDateTime;
import java.util.List;

public class Reserva {
  private Long id;
  private String codigo;
  private Asiento asiento;
  private List<Tramo> tramo;
  private Usuario idUsuario;
  private EstadoDeReserva estado;
  private LocalDateTime inicioDeReserva;

  public Reserva(Long id, Asiento asiento, List<Tramo> tramo, Usuario idUsuario) {
    this.id = id;
    this.asiento = asiento;
    this.tramo = tramo;
    this.idUsuario = idUsuario;
  }

  public void reservar() {
    asiento.esReservado();
    this.estado = EstadoDeReserva.PENDENTE;
    this.inicioDeReserva = LocalDateTime.now();
  }

  public void cancelar() {
    asiento.estaLibre();
    this.estado = EstadoDeReserva.CANCELADA;
  }

  public void confirmar() {
    asiento.estaConfirmada();
    this.estado = EstadoDeReserva.CONFIRMADO;
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
