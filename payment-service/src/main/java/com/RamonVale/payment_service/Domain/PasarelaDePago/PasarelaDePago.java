package com.RamonVale.payment_service.Domain.PasarelaDePago;

import com.RamonVale.payment_service.Domain.Pago.Pago;

public interface PasarelaDePago {
  void cobrar(Pago pago);
}
