package com.RamonVale.financial_service.Repository;

import com.RamonVale.financial_service.Domain.wallet.CorporateAccount;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorporateAccountRepository extends JpaRepository<CorporateAccount, UUID> {
}
