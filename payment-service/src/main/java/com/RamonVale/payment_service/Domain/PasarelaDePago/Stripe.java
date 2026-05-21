package com.RamonVale.payment_service.Domain.PasarelaDePago;

import com.RamonVale.payment_service.Domain.Pago.Pago;

public class Stripe implements PasarelaDePago {

  @Override
  public void cobrar(Pago pago) {
    pago.esCobrado();
  }
}
