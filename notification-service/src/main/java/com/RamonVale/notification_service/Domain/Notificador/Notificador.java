package com.RamonVale.notification_service.Domain.Notificador;

import com.RamonVale.notification_service.Domain.Notificacion.Notificacion;

public interface Notificador {
  void enviar(Notificacion notification);
}
