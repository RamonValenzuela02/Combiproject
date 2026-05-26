package com.RamonVale.financial_service.Controller;

import com.RamonVale.financial_service.Domain.ledger.LedgerEntry;
import com.RamonVale.financial_service.Service.FinancialTransactionService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/financial/")
public class TransactionController {
  private FinancialTransactionService transactionService;

  @GetMapping("ledger/{userId}")
  public List<LedgerEntry> getTransaction(@PathVariable Long userId) {
    return transactionService.getEntradasDe(userId);
  }

}
