package com.RamonVale.financial_service.Repository;

import com.RamonVale.financial_service.Domain.wallet.VirtualCard;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirtualCardRepository extends JpaRepository<VirtualCard, UUID> {

}
