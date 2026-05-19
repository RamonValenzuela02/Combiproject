package com.RamonVale.booking_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.RamonVale.booking_service.Domain.Asiento.Asiento;
import com.RamonVale.booking_service.Domain.Asiento.EstadoAsiento;
import com.RamonVale.booking_service.Domain.Automovil.Automovil;
import com.RamonVale.booking_service.Domain.Chofer.Chofer;
import com.RamonVale.booking_service.Domain.Pasajero.Pasajero;
import com.RamonVale.booking_service.Domain.Reserva.EstadoDeReserva;
import com.RamonVale.booking_service.Domain.Reserva.Reserva;
import com.RamonVale.booking_service.Domain.Usuario.Usuario;
import com.RamonVale.booking_service.Domain.Viaje.TipoDeViaje;
import com.RamonVale.booking_service.Domain.Viaje.Tramo;
import com.RamonVale.booking_service.Domain.Viaje.Viaje;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservaTest {
  Viaje viaje;
  Reserva reserva;
  Usuario usuario;
  Pasajero pasajero;
  List<Tramo> tramosAHacer;
  TipoDeViaje tipoDeViaje;
  Automovil automovil;
  List<Tramo> tramosACompletar;
  Tramo tramo1;
  Tramo tramo2;
  Tramo tramo3;

  @BeforeEach
  void setUp() {
    tramo1 = new Tramo("Lujan", "Moreno", 10);
    tramo2 = new Tramo("Moreno", "Boedo", 10);
    tramo3 = new Tramo("Boedo", "Once", 10);
    tramosACompletar = Arrays.asList(tramo1, tramo2, tramo3);
    tramosAHacer = Arrays.asList(tramo1, tramo2);

    automovil = new Automovil("ABC",new Chofer(),3);
    tipoDeViaje = new TipoDeViaje("BA", "Lujan", tramosACompletar);
    viaje = new Viaje(tipoDeViaje, LocalDateTime.now().plusHours(2), automovil);

    usuario = new Usuario();
    pasajero = new Pasajero(usuario);

  }

  @Test
  void validoPrecioViajeCorrecto(){
    Asiento asiento =  viaje.getAsientos().get(0);

    assertEquals(30, viaje.precioViajeCompleto());
  }

  @Test
  void validarEstadoPostRealizacionDeReserva(){
    Asiento asiento1 =  viaje.getAientosLibres().get(0);
    Reserva reserva1 = new Reserva(1L, asiento1 , tramosAHacer, pasajero);

    reserva1.reservar();

    assertEquals(EstadoAsiento.RESERVADO, reserva1.getAsiento().getEstado());
    assertEquals(EstadoDeReserva.PENDENTE, reserva1.getEstado());
  }

  @Test
  void confirmarReserva(){
    Asiento asiento1 =  viaje.getAientosLibres().get(0);
    Reserva reserva1 = new Reserva(1L, asiento1 , tramosAHacer, pasajero);

    reserva1.reservar();
    reserva1.confirmar();

    assertEquals(EstadoAsiento.OCUPADO, reserva1.getAsiento().getEstado());
    assertEquals(EstadoDeReserva.CONFIRMADO, reserva1.getEstado());
  }

  @Test
  void calcecalReserva(){
    Asiento asiento1 =  viaje.getAientosLibres().get(0);
    Reserva reserva1 = new Reserva(1L, asiento1 , tramosAHacer, pasajero);

    reserva1.reservar();
    reserva1.cancelar();

    assertEquals(EstadoAsiento.LIBRE, reserva1.getAsiento().getEstado());
    assertEquals(EstadoDeReserva.CANCELADA, reserva1.getEstado());
  }

  //VALIDACIONES DE INSTANCEACION ERRONES O ERRORES
  @Test
  void noDeberiaCancelarDosVeces() {
    Asiento asiento = viaje.getAsientos().get(0);
    Reserva reserva = new Reserva(1L, asiento, tramosAHacer, pasajero);

    reserva.reservar();
    reserva.cancelar();

    assertThrows(RuntimeException.class, reserva::cancelar);
  }

  @Test
  void noDeberiaConfirmarDosVeces() {
    Asiento asiento = viaje.getAsientos().get(0);
    Reserva reserva = new Reserva(1L, asiento, tramosAHacer, pasajero);

    reserva.reservar();
    reserva.confirmar();

    assertThrows(RuntimeException.class, reserva::confirmar);
  }

  @Test
  void noDeberiaConfirmarSinReservarAntes() {
    Asiento asiento = viaje.getAsientos().get(0);
    Reserva reserva = new Reserva(1L, asiento, tramosAHacer, pasajero);

    assertThrows(RuntimeException.class, reserva::confirmar);

  }

  }
