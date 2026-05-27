package com.RamonVale.financial_service.Repository;

import com.RamonVale.financial_service.Domain.ledger.Account;
import com.RamonVale.financial_service.Domain.ledger.AccountType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
  Optional<Account> findByOwnerIdAndAccountType(String ownerId, AccountType type);
}
