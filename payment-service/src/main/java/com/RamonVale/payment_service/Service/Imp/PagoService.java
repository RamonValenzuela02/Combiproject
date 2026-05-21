package com.RamonVale.payment_service.Service.Imp;

import com.RamonVale.payment_service.Domain.Pago.Pago;
import com.RamonVale.payment_service.Domain.PasarelaDePago.PasarelaDePago;
import com.RamonVale.payment_service.Kafka.KafkaProducer;
import com.RamonVale.payment_service.Repository.PagoRepository;
import com.RamonVale.payment_service.Service.IPagoService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class PagoService implements IPagoService {
  private final PagoRepository pagoRepository;
  private final Map<String, PasarelaDePago> pasarelas;
  private final KafkaProducer kafkaProducer;

  public PagoService(PagoRepository pagoRepository, Map<String, PasarelaDePago> pasarelas ,KafkaProducer kafkaProducer) {
    this.pagoRepository = pagoRepository;
    this.pasarelas = pasarelas;
    this.kafkaProducer = kafkaProducer;
  }


  @Override
  public Pago createPago(String codigoReserva, String usuarioId, double monto, LocalDateTime fechaExpiracion) {
    Pago pago = new Pago(codigoReserva, usuarioId, monto, fechaExpiracion);
    return pagoRepository.save(pago);
  }

  @Override
  public String iniciarPago(String codigoReserva, String medioPago) {
    Pago pago = pagoRepository.findById(codigoReserva)
      .orElseThrow(() -> new RuntimeException("Pago not found"));

    PasarelaDePago pasarela = pasarelas.get(medioPago);
    if (pasarela == null) {
      throw new RuntimeException("Medio de pago no soportado");
    }
    String linkPago = pasarela.generarLinkDePago(pago);
    pago.iniciarPago(medioPago, linkPago);
    pagoRepository.save(pago);

    return linkPago;
  }

  @Override
  public void procesarWebhook(String medioPago, Map<String, Object> payload) {
    String pagoId = (String) payload.get("external_reference");
    Pago pago = pagoRepository.findById(pagoId)
      .orElseThrow(() -> new RuntimeException("Pago not found"));

    PasarelaDePago pasarela = pasarelas.get(medioPago);
    boolean aprobado = pasarela.procesarWebhook(payload);

    if (aprobado) {
      pago.aprobar();
      kafkaProducer.publicar("payment_confirm", pago);
    }else {
      pago.rechazar();
    }
    pagoRepository.save(pago);
  }

  @Override
  public void cancelarPago(String codigoReserva) {
    Pago pago = pagoRepository.findByCodigoReserva(codigoReserva)
      .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    pago.cancelar();
    pagoRepository.save(pago);
  }
}
