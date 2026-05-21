package com.RamonVale.payment_service.Repository;

import com.RamonVale.payment_service.Domain.Pago.Pago;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PagoRepository extends MongoRepository<Pago, String> {
  Optional<Pago> findByCodigoReserva(String codigoReserva);
}
