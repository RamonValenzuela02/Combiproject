package com.RamonVale.financial_service.Controller;

import com.RamonVale.financial_service.Service.AccountService;
import java.math.BigDecimal;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/financial/account")
public class AccountController {
  private AccountService accountService;

  @GetMapping("/{idUser}/balance")
  public ResponseEntity<BigDecimal> getBalance(@PathVariable Long idUser) {
    return accountService.getBalance(idUser);
  }
}
