package com.RamonVale.payment_service.Service;

import com.RamonVale.payment_service.Domain.Pago.Pago;
import java.time.LocalDateTime;
import java.util.Map;

public interface IPagoService {
  Pago createPago(String codigoReserva, String usuarioId, double monto, LocalDateTime fechaExpiracion);
  String iniciarPago(String codigoReserva, String medioPago);
  void procesarWebhook(String medioPago, Map<String, Object> payload);
  void cancelarPago(String codigoReserva);
}
