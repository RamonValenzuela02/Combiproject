package com.RamonVale.financial_service.Domain.wallet;

import java.math.BigDecimal;
import java.util.List;

public class CorporateAccount {
  private Long id;
  private String companyName;
  private Wallet wallet;
  private BigDecimal limit;
  private boolean active;
  private List<EmployeeQuota> employeeQuota;
}
