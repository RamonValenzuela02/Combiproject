package com.RamonVale.notification_service.Domain.Notificacion;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
//@Document(collation = "Notificaciones")
public class Notificacion {
  //@Id
  private String id;
  private String email;
  private String telefono;
  private String mensaje;

  public Notificacion(String email, String telefono, String mensaje) {
    this.email = email;
    this.telefono = telefono;
    this.mensaje = mensaje;
  }
}
