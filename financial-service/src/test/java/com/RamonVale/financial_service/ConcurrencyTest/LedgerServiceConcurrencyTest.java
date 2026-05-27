package com.RamonVale.financial_service.ConcurrencyTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.RamonVale.financial_service.Repository.AccountRepository;
import com.RamonVale.financial_service.Service.LedgerService;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class LedgerServiceConcurrencyTest {
  @Container
  static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
    .withDatabaseName("smartcombi_test")
    .withUsername("test")
    .withPassword("test");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url",      mysql::getJdbcUrl);
    registry.add("spring.datasource.username", mysql::getUsername);
    registry.add("spring.datasource.password", mysql::getPassword);
  }

  @Autowired
  LedgerService ledgerService;
  @Autowired
  AccountRepository accountRepo;

  private String passengerId;
  private String driverId;

  @BeforeEach
  void setUp() {
    passengerId = UUID.randomUUID().toString();
    driverId    = UUID.randomUUID().toString();
  }

  // ─────────────────────────────────────────────────────────────────────────

  @Test
  @DisplayName("El ledger siempre cuadra: SUM(débitos) == SUM(créditos)")
  void ledgerAlwaysBalances() {
    // Procesar 20 viajes distintos
    for (int i = 0; i < 20; i++) {
      var tx = ledgerService.processTrip(
        "reserva-balance-" + i,
        passengerId,
        driverId,
        new BigDecimal("10000.00")
      );
      assertThat(tx.validateBalance())
        .as("Viaje %d: débitos deben igualar créditos", i)
        .isTrue();
    }
  }

  // ─────────────────────────────────────────────────────────────────────────

  @Test
  @DisplayName("Concurrencia: 20 viajes simultáneos no producen balance incorrecto")
  void concurrentTripsProduceCorrectBalance() throws InterruptedException {
    int threadCount      = 20;
    BigDecimal fareEach  = new BigDecimal("1000.00");
    BigDecimal expected  = fareEach.multiply(BigDecimal.valueOf(threadCount))
      .multiply(new BigDecimal("0.50"));

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch startGate = new CountDownLatch(1);
    CountDownLatch  doneLatch  = new CountDownLatch(threadCount);
    List<Exception> errors     = new CopyOnWriteArrayList<>();

    for (int i = 0; i < threadCount; i++) {
      final int tripNum = i;
      executor.submit(() -> {
        try {
          startGate.await(); // todos arrancan a la vez
          ledgerService.processTrip(
            "concurrent-trip-" + tripNum,
            passengerId,
            driverId,
            fareEach
          );
        } catch (Exception e) {
          errors.add(e);
        } finally {
          doneLatch.countDown();
        }
      });
    }

    startGate.countDown(); // ¡largan todos al mismo tiempo!
    doneLatch.await(30, TimeUnit.SECONDS);
    executor.shutdown();

    assertThat(errors).as("Ningún viaje debería fallar").isEmpty();

    // El balance del chofer debe ser exactamente 50% * total de viajes.
    BigDecimal driverBalance = ledgerService.getBalance(driverId, true);
    assertThat(driverBalance)
      .as("Balance del chofer incorrecto — posible lost update")
      .isEqualByComparingTo(expected);
  }

  // ─────────────────────────────────────────────────────────────────────────

  @Test
  @DisplayName("Idempotencia: el mismo PagoConfirmado procesado 3 veces no duplica el balance")
  void idempotentProcessing() {
    String reservaId = "idempotent-reserva-001";
    BigDecimal fare  = new BigDecimal("5000.00");

    // Simular que kafka entrega 3 veces el mismo evento
    ledgerService.processTrip(reservaId, passengerId, driverId, fare);
    ledgerService.processTrip(reservaId, passengerId, driverId, fare);
    ledgerService.processTrip(reservaId, passengerId, driverId, fare);

    // El balance del chofer debe ser como si solo se procesó una vez.
    BigDecimal driverBalance    = ledgerService.getBalance(driverId, true);
    BigDecimal expectedBalance  = fare.multiply(new BigDecimal("0.80"));

    assertThat(driverBalance)
      .as("Idempotencia rota — el balance se triplicó")
      .isEqualByComparingTo(expectedBalance);
  }

  // ─────────────────────────────────────────────────────────────────────────

  @Test
  @DisplayName("El balance del pasajero es negativo si no hay saldo suficiente")
  void insufficientFundsThrowsException() {
    // En el modelo actual, el pasajero puede quedar con balance negativo
    // porque el pago ya fue confirmado por MercadoPago.
    // Este test documenta ese comportamiento esperado.
    assertThat(
      ledgerService.getBalance(passengerId, false)
    ).isEqualByComparingTo(BigDecimal.ZERO);

    // Después de un viaje, el pasajero debería tener balance negativo
    // (es un wallet de registro, no pre-cargado).
    ledgerService.processTrip("trip-001", passengerId, driverId, new BigDecimal("3000.00"));

    BigDecimal passengerBalance = ledgerService.getBalance(passengerId, false);
    assertThat(passengerBalance)
      .as("El pasajero debe tener balance negativo después de pagar")
      .isLessThan(BigDecimal.ZERO);
  }
}
