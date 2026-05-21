package com.RamonVale.payment_service.Domain.Pago;

public enum EstadoPago {
  PENDIENTE_WEBHOOK,
  APROBADO,
  RECHAZADO,
  REEMBOLSADO,
  FALLIDO
}
