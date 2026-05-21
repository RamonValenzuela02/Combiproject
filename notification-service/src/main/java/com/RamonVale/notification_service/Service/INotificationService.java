package com.RamonVale.notification_service.Service;

import com.RamonVale.notification_service.Domain.BusEvent;

public interface INotificationService {
  void notificar(BusEvent event, String mensaje);
}
