package com.RamonVale.notification_service.Consumers;

import com.RamonVale.notification_service.Domain.BusEvent;
import com.RamonVale.notification_service.Service.Imp.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
  private final NotificationService notificationService;

  public KafkaConsumer(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @KafkaListener(topics = "asciento_reservado", groupId = "notification-service")
  public void onAsientoReservado(BusEvent event) {
    String mensaje = String.format(
      "Hola %s, su asiento %s ha sido reservado de %s a %s a las %s, " +
        "por favor realiza el pago dentro de 30 minutos o sino su asiento va a ser cancelado",
      event.getNombrePasajero(),
      event.getNumeroDeAsiento(),
      event.getOrigen(),
      event.getDestino(),
      event.getFechaHora()
    );
    notificationService.notificar(event, mensaje);
  }

  @KafkaListener(topics = "asiento_confirmado", groupId = "notification-service")
  public void onAsientoConfirmado(BusEvent event) {
    String mensaje = String.format(
      "Hola %s, su asiento %s ha sido confirmado de %s a %s a las %s, " +
        "por favor le aconsejamos que este 15 mins antes de la salida del bus",
      event.getNombrePasajero(),
      event.getNumeroDeAsiento(),
      event.getOrigen(),
      event.getDestino(),
      event.getFechaHora()
    );
    notificationService.notificar(event, mensaje);
  }

  @KafkaListener(topics = "comienzo_deBus", groupId = "notification-service")
  public void onComienzoDeBus(BusEvent event) {
    String mensaje = String.format(
      "Hola %s, su viaje a comenzado de %s a %s a las %s, " ,
      event.getNombrePasajero(),
      event.getNumeroDeAsiento(),
      event.getOrigen(),
      event.getDestino(),
      event.getFechaHora()
    );
    notificationService.notificar(event, mensaje);
  }

  @KafkaListener(topics = "finalizacion_deBus", groupId = "notification-service")
  public void onFinalizacionDeBus(BusEvent event) {
    String mensaje = String.format(
      "Hola %s, su viaje a finalizado exitosamente " ,
      event.getNombrePasajero(),
      event.getNumeroDeAsiento(),
      event.getOrigen(),
      event.getDestino(),
      event.getFechaHora()
    );
    notificationService.notificar(event, mensaje);
  }

}
