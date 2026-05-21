package com.RamonVale.notification_service.Service.Imp;

import com.RamonVale.notification_service.Domain.BusEvent;
import com.RamonVale.notification_service.Domain.Notificacion.Notificacion;
import com.RamonVale.notification_service.Domain.Notificador.EmailSender;
import com.RamonVale.notification_service.Domain.Notificador.SmsSender;
import com.RamonVale.notification_service.Repository.NotificationRepository;
import com.RamonVale.notification_service.Service.INotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService {
  private final EmailSender emailSender;
  private final SmsSender smsSender;
  //private final NotificationRepository notificacionRepository;

  public NotificationService(EmailSender emailSender, SmsSender smsSender, NotificationRepository notificacionRepository, NotificationRepository notificationRepository) {
    this.emailSender = emailSender;
    this.smsSender = smsSender;
    //this.notificacionRepository = notificacionRepository;
  }

  private void enviar(Notificacion notificacion) {
    //en el futuro vamos a corregir esto (poco extensible)
    //tendriamos que modelar a los usuarios como un mongodb en common
    //y agregarle un obsevadoresDeNotificacion o algo asi
    emailSender.enviar(notificacion);
    smsSender.enviar(notificacion);
    //notificationRepository.save(notificacion);
  }

  @Override
  public void notificar(BusEvent event, String mensaje) {
    Notificacion notificacion = new Notificacion(
      event.getEmail(),
      event.getTelefono(),
      mensaje
    );
    enviar(notificacion);
  }

}
