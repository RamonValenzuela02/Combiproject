package com.RamonVale.financial_service.Repository;

import com.RamonVale.financial_service.Domain.ledger.EntryType;
import com.RamonVale.financial_service.Domain.ledger.FinancialTransaction;
import com.RamonVale.financial_service.Domain.ledger.LedgerEntry;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, UUID> {
  // idempotencia
  boolean existsByIdempotencyKey(String idempotencyKey);
  Optional<FinancialTransaction> findByIdempotencyKey(String idempotencyKey);

  // reversal
  Optional<FinancialTransaction> findByReferenceId(String referenceId);
  boolean existsByReferenceId(String referenceId);

  // historial del wallet de un usuario — busca las entries de sus cuentas
  @Query("""
        SELECT e FROM FinancialTransaction t
        JOIN t.entries e
        WHERE e.accountId = :accountId
        ORDER BY t.createdAt DESC
        """)
  Page<LedgerEntry> findEntriesByAccountId(
    @Param("accountId") UUID accountId,
    Pageable pageable);

  // totales para reconciliación nocturna
  @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM FinancialTransaction t
        JOIN t.entries e
        WHERE e.entryType = :tipo
          AND t.createdAt >= :desde
          AND t.createdAt < :hasta
        """)
  BigDecimal sumEntriesByTypeAndPeriod(
    @Param("tipo")  EntryType tipo,
    @Param("desde") Instant desde,
    @Param("hasta") Instant hasta);

  BigDecimal sumByEntryTypeAndPeriod(EntryType entryType, Instant startOfDay, Instant endOfDay);
}
