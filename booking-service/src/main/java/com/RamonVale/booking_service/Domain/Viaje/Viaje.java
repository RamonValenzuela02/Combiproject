package com.RamonVale.booking_service.Domain.Viaje;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Automovil.Automovil;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Viaje {
  private Long id;
  private EstadoViaje estadoViaje;
  private LocalDateTime fechaSalida;
  private TipoDeViaje tipoDeViaje;
  private List<Asiento> asientos;
  private List<Tramo> tramos;
  private Automovil automovil;

  public Viaje(TipoDeViaje tipoDeViaje, LocalDateTime fechaSalida, Automovil automovil) {
    this.tipoDeViaje = tipoDeViaje;
    this.fechaSalida = fechaSalida;
    this.automovil = automovil;
    this.estadoViaje = EstadoViaje.PENDENTE;
    generarAsientos();
  }

  public double precioViajeCompleto() {
    return tramos.stream()
      .mapToDouble(tramo -> tramo.getValor())
      .sum();
  }

  public double tiempoRestanteEnHorasAntesDePartida(LocalDateTime tiempo) {
    Duration duracion = Duration.between(fechaSalida, tiempo);
    return duracion.toHours();
  }


  //TODO no parece bien que esto este aca
  public void generarAsientos() {
    int n = automovil.getCantidadDeAsientos();
    for (int i = 0; i < n; i++) {
      Asiento asiento = new Asiento(this);
      asientos.add(asiento);
    }
  }

}
