package com.RamonVale.notification_service.Domain.Notificador;

import com.RamonVale.notification_service.Domain.Notificacion.Notificacion;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SmsSender implements Notificador{
  private String acountId;
  private String authToken;
  private String fromNumber;


  @Override
  public void enviar(Notificacion notification) {
    Twilio.init(acountId, authToken);
    Message.creator(
      new com.twilio.type.PhoneNumber(notification.getTelefono()),
      new com.twilio.type.PhoneNumber(fromNumber),
      notification.getMensaje()
    ).create();
  }
}
