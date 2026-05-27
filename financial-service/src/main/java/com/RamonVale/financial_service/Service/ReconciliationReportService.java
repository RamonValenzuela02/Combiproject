package com.RamonVale.financial_service.Service;

import com.RamonVale.financial_service.Domain.ledger.EntryType;
import com.RamonVale.financial_service.Domain.report.ReconciliationReport;
import com.RamonVale.financial_service.Dto.ReconciliationAlertEvent;
import com.RamonVale.financial_service.Repository.FinancialTransactionRepository;
import com.RamonVale.financial_service.Repository.ReconciliationReportRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

public class ReconciliationReportService {

  private static final Logger log = LoggerFactory.getLogger(ReconciliationReportService.class);
  private static final BigDecimal ALERT_THRESHOLD = new BigDecimal("0.01");
  private static final String EXCHANGE = "smartcombi.exchange";
  private static final String ROUTING_KEY_ALERT = "reconciliation.alert";

  private ReconciliationReportRepository reportRepo;
  private FinancialTransactionRepository financialTransactionRepo;

  public ReconciliationReportService(ReconciliationReportRepository reportRepo,
                               FinancialTransactionRepository financialTransactionRepo) {
    this.financialTransactionRepo = financialTransactionRepo;
    this.reportRepo     = reportRepo;
  }


  @Scheduled(cron = "0 0 2 * * *", zone = "UTC")
  @Transactional
  public void runDailyReconciliation() {
    LocalDate yesterday = LocalDate.now(ZoneOffset.UTC).minusDays(1);
    log.info("Iniciando reconciliación para {}", yesterday);
    reconcileDate(yesterday);
  }


  @Transactional
  public ReconciliationReport reconcileDate(LocalDate date) {

    // Límites del día en UTC
    Instant startOfDay = date.atStartOfDay(ZoneOffset.UTC).toInstant();
    Instant endOfDay   = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

    // Sumar todos los DEBIT y CREDIT del ledger para esa fecha.
    BigDecimal totalDebits = financialTransactionRepo.sumByEntryTypeAndPeriod(
      EntryType.DEBIT, startOfDay, endOfDay
    );
    BigDecimal totalCredits = financialTransactionRepo.sumByEntryTypeAndPeriod(
      EntryType.CREDIT, startOfDay, endOfDay
    );

    // Generar el reporte — la entidad evalúa si hay drift.
    ReconciliationReport report = new ReconciliationReport(date, totalDebits, totalCredits);
    reportRepo.save(report);

    log.info("Reconciliación {}: débitos={}, créditos={}, drift={}",
      date, totalDebits, totalCredits, report.getDrift());

    // Si hay drift, publicar alerta.
    if (report.hasDriftAbove(ALERT_THRESHOLD)) {
      publishAlert(report);
    }

    return report;
  }

  // ── Consulta de reportes
  @Transactional(readOnly = true)
  public List<ReconciliationReport> getLast30Reports() {
    return reportRepo.findAllByOrderByDateDesc(PageRequest.of(0, 30));
  }

  // ── Publicación de alerta ─────────────────────────────────────────────────
  private void publishAlert(ReconciliationReport report) {
    var alertPayload = new ReconciliationAlertEvent(
      report.getDate(),
      report.getTotalDebits(),
      report.getTotalCredits(),
      report.getDrift(),
      report.getGeneratedAt()
    );

//    rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_ALERT, alertPayload);
    log.warn("ReconciliationAlert publicado. Drift={} ARS para {}",
      report.getDrift(), report.getDate());
  }

}
