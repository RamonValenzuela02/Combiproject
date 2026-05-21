package com.RamonVale.payment_service.Controller;

import com.RamonVale.payment_service.Domain.Pago.Pago;
import com.RamonVale.payment_service.Service.Imp.PagoService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagos")
public class PagoController {

  private final PagoService pagoService;

  public PagoController(PagoService pagoService) {
    this.pagoService = pagoService;
  }

  @PostMapping("/{codigoReserva}/iniciar")
  public ResponseEntity<String> iniciarPago(@PathVariable String codigoReserva,
                                            @RequestParam String medioPago) {
    String linkPago = pagoService.iniciarPago(codigoReserva, medioPago);

    return ResponseEntity.ok(linkPago);
  }

  @PostMapping("/webhook/{medioPago}")
  public ResponseEntity<Void> webhook(
    @PathVariable String medioPago,
    @RequestBody Map<String, Object> payload
  ) {
    pagoService.procesarWebhook(medioPago, payload);
    return ResponseEntity.ok().build();
  }
}
