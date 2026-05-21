package com.RamonVale.notification_service.Domain.Notificador;

import com.RamonVale.notification_service.Domain.Notificacion.Notificacion;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSender implements Notificador{
  private final JavaMailSender mailSender;

  public EmailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void enviar(Notificacion notification) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(notification.getEmail());
    message.setSubject("Notificacion de tu viaje");
    message.setText(notification.getMensaje());
    mailSender.send(message);
  }
}
