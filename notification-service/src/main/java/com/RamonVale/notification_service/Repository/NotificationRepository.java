package com.RamonVale.notification_service.Repository;

import com.RamonVale.notification_service.Domain.Notificacion.Notificacion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notificacion, String> {
}
