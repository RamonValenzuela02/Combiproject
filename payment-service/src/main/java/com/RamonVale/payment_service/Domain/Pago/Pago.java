package com.RamonVale.payment_service.Domain.Pago;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pago {
  private String id;
  private String codigoReserva;
  private String usuarioId;
  private double monto;
  private EstadoPago estado;
  private String linkPago;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaExpiracion;
  private String medioPago;

  public Pago(String codigoReserva,
              String usuarioId,
              double monto,
              LocalDateTime fechaExpiracion) {
    this.codigoReserva = codigoReserva;
    this.usuarioId = usuarioId;
    this.monto = monto;
    this.estado = EstadoPago.PENDIENTE;
    this.fechaCreacion = LocalDateTime.now();
    this.fechaExpiracion = fechaExpiracion;
  }

  public void iniciarPago(String medioPago, String linkPago) {
    if (this.estado != EstadoPago.PENDIENTE) {
      throw new IllegalStateException("el estado del pago no esta en PENDIENTE");
    }
    this.estado = EstadoPago.EN_PROCESO;
    this.linkPago = linkPago;
    this.medioPago = medioPago;
  }

  public void aprobar() {
    if (this.estado != EstadoPago.EN_PROCESO) {
      throw new IllegalStateException("el estado del pago no esta en EN_PROCESO");
    }
    this.estado = EstadoPago.APROBADO;
  }

  public void rechazar() {
    if (this.estado != EstadoPago.EN_PROCESO) {
      throw new IllegalStateException("el estado del pago no esta en EN_PROCESO");
    }
    this.estado = EstadoPago.RECHAZADO;
  }

  public void cancelar() {
    if (this.estado != EstadoPago.PENDIENTE) {
      throw new IllegalStateException("el estado del pago no esta en PENDIENTE");
    }
    this.estado = EstadoPago.CANCELADO;
  }



}
