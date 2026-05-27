package com.RamonVale.financial_service.Repository;

import com.RamonVale.financial_service.Domain.wallet.CardHold;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardHoldRepository extends JpaRepository<CardHold, UUID> {
  Optional<CardHold> findByPomeloTxId(String pomeloTxId);

  @Query("""
    SELECT COALESCE(SUM(h.amount), 0)
    FROM CardHold h
    WHERE h.userId = :userId
      AND h.settledAt IS NULL
      AND h.reversedAt IS NULL
      AND h.expiresAt > :now
    """)
  BigDecimal sumActiveHoldsByUserId(
    @Param("userId") UUID userId,
    @Param("now") Instant now
  );

  @Query("""
    SELECT h FROM CardHold h
    WHERE h.expiresAt < :now
      AND h.settledAt IS NULL
      AND h.reversedAt IS NULL
    """)
  List<CardHold> findExpiredPending(@Param("now") Instant now);

}
