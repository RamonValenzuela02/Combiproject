package com.RamonVale.financial_service.Repository;

import com.RamonVale.financial_service.Domain.report.ReconciliationReport;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReconciliationReportRepository extends JpaRepository<ReconciliationReport, UUID> {
  List<ReconciliationReport> findAllByOrderByDateDesc(PageRequest of);
}
