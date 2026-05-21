package com.RamonVale.payment_service.Domain.PasarelaDePago;

import com.RamonVale.payment_service.Domain.Pago.Pago;
import java.util.Map;
import java.util.Objects;

public interface PasarelaDePago {
  String generarLinkDePago(Pago pago);
  boolean procesarWebhook(Map<String, Object> payload);
}
