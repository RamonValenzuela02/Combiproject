package com.RamonVale.financial_service.Repository;

import com.RamonVale.financial_service.Domain.ledger.Account;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
