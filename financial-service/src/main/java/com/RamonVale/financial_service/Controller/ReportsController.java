package com.RamonVale.financial_service.Controller;

import com.RamonVale.financial_service.Domain.ReconciliationReport;
import com.RamonVale.financial_service.Service.ReconciliationReportService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/financial/reports")
public class ReportsController {
  private ReconciliationReportService reportsService;

  //falta la paginacion para mas adelante
  @GetMapping
  public List<ReconciliationReport> getReports() {
    return reportsService.getReports();
  }

}
