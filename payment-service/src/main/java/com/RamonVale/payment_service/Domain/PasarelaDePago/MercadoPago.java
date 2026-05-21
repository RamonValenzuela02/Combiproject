package com.RamonVale.payment_service.Domain.PasarelaDePago;

import com.RamonVale.payment_service.Domain.Pago.Pago;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MercadoPago implements PasarelaDePago {
  private final String accesToken;

  public MercadoPago(String accesToken) {
    this.accesToken = accesToken;
  }

  @Override
  public String generarLinkDePago(Pago pago) {
    try {
      MercadoPagoConfig.setAccessToken(accesToken);

      PreferenceItemRequest item = PreferenceItemRequest.builder()
        .title("Reserva" + pago.getCodigoReserva())
        .quantity(1)
        .unitPrice(BigDecimal.valueOf(pago.getMonto()))
        .build();

      PreferenceRequest request = PreferenceRequest.builder()
        .items(List.of(item))
        .externalReference(pago.getId())
        .build();

      PreferenceClient client = new PreferenceClient();
      Preference preference = client.create(request);

      return preference.getInitPoint();
    }catch (Exception e){
      throw new RuntimeException("Error al generar el link de MercadoPago", e);
    }
  }

  @Override
  public boolean procesarWebhook(Map<String, Object> payload) {
    String status = (String) payload.get("status");
    return  "approved".equals(status);
  }
}
