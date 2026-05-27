package com.RamonVale.financial_service.Repository;

import com.RamonVale.financial_service.Domain.wallet.EmployeeQuota;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeQuotaRepository extends JpaRepository<EmployeeQuota, UUID> {
  Optional<EmployeeQuota> findByCorporateAccountIdAndEmployeeUserId(UUID corporateAccountId, UUID employeeUserId);
}
