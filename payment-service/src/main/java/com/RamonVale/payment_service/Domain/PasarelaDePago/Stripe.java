package com.RamonVale.payment_service.Domain.PasarelaDePago;

import com.RamonVale.payment_service.Domain.Pago.Pago;
import com.stripe.StripeClient;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.util.Map;

public class Stripe implements PasarelaDePago {
  private final String secretKey;

  public Stripe(String secretKey) {
    this.secretKey = secretKey;
  }

  @Override
  public String generarLinkDePago(Pago pago) {
    try {
      StripeClient client = new StripeClient(secretKey);

      SessionCreateParams params = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl("https://miapp.com/pagos/success")
        .setCancelUrl("https://miapp.com/pagos/cancel")
        .setClientReferenceId(pago.getId())
        .addLineItem(SessionCreateParams.LineItem.builder()
          .setQuantity(1L)
          .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
            .setCurrency("ars")
            .setUnitAmount((long) (pago.getMonto() * 100))
            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
              .setName("Reserva " + pago.getCodigoReserva())
              .build())
            .build())
          .build())
        .build();

      Session session = client.checkout().sessions().create(params);
      return session.getUrl();
    }catch (Exception e) {
      throw new RuntimeException("Error al generar el link de stripe",e);
    }
  }

  @Override
  public boolean procesarWebhook(Map<String, Object> payload) {
    String status = (String) payload.get("payment_status");
    return "paid".equals(status);
  }
}
