package com.RamonVale.financial_service.Repository;

import com.RamonVale.financial_service.Domain.wallet.Advance;
import com.RamonVale.financial_service.Domain.wallet.AdvanceStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvanceRepository extends JpaRepository<Advance, UUID> {
  List<Advance> findByDriverIdAndStatus(UUID driverId, AdvanceStatus advanceStatus);

  BigDecimal sumActiveAdvancesByDriverId(UUID userId);
}
