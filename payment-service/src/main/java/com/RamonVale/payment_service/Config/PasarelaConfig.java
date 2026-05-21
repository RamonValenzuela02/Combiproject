package com.RamonVale.payment_service.Config;

import com.RamonVale.payment_service.Domain.PasarelaDePago.MercadoPago;
import com.RamonVale.payment_service.Domain.PasarelaDePago.PasarelaDePago;
import com.RamonVale.payment_service.Domain.PasarelaDePago.Stripe;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasarelaConfig {

  @Bean
  public Map<String, PasarelaDePago> pasarelas(
    @Value("${mercadopago.access-token}") String mercadoPagoToken,
    @Value("${stripe.secret-key}") String stripeKey
  ) {
    return Map.of(
      "MERCADO-PAGO", new MercadoPago(mercadoPagoToken),
      "STRIPE", new Stripe(stripeKey)
    );
  }
}
