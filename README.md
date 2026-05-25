# SmartCombi — Embedded Finance Platform

Sistema de transporte basado en microservicios con capacidades de Embedded Finance, incluyendo wallets digitales, ledger de doble entrada, adelanto de ganancias, cuentas corporativas y tarjetas virtuales integradas con Pomelo Sandbox.

## Arquitectura

La plataforma está compuesta por múltiples microservicios desacoplados mediante eventos con RabbitMQ:

- API Gateway
- Core Service
- Payment Service
- Notification Service
- Financial Service
- Wallet Service

## Embedded Finance Features

### Ledger de doble entrada

Implementación de un sistema contable financiero donde cada transacción genera débitos y créditos inmutables, garantizando consistencia financiera.

### Wallets digitales

- Wallet para pasajeros
- Wallet para choferes
- Balance en tiempo real
- Historial financiero paginado

### Adelanto de ganancias

Los choferes pueden solicitar adelantos de hasta el 70% de sus ganancias acumuladas, simulando un sistema de factoring interno.

### Cuenta corporativa

Empresas pueden:
- cargar saldo corporativo,
- asignar cupos mensuales a empleados,
- monitorear gastos en tiempo real.

### Tarjetas virtuales (Pomelo Sandbox)

Integración con Pomelo para emisión de tarjetas virtuales:
- autorización en menos de 500ms,
- balance cacheado en Redis,
- manejo de holds y settlements,
- cumplimiento básico PCI-DSS.

## Tecnologías utilizadas

### Backend

- Java
- Spring Boot
- Spring Security
- Spring Cloud Gateway
- Spring Data JPA

### Infraestructura

- PostgreSQL
- Redis
- RabbitMQ
- Docker
- Keycloak

### Integraciones Fintech

- Mercado Pago
- Pomelo Sandbox
- SendGrid
- Twilio

## Conceptos Fintech implementados

- Embedded Finance
- Double-entry Ledger
- Idempotencia
- Outbox Pattern
- Event-Driven Architecture
- Optimistic Locking
- Reconciliation Process
- Wallet Infrastructure
- Card Authorization Flow
- Balance Holds
- Eventual Consistency

## Flujo financiero

1. El pasajero reserva y paga un viaje.
2. Payment Service publica `PagoConfirmado`.
3. Financial Service genera:
   - débito al pasajero,
   - crédito al chofer,
   - comisión de plataforma.
4. Wallet Service actualiza balances y funcionalidades financieras.
5. Reconciliation Scheduler valida diariamente la integridad del ledger.

## Testing

El proyecto incluye:
- tests de concurrencia,
- validaciones de idempotencia,
- reconciliación financiera,
- simulación de pagos duplicados,
- integración con Testcontainers y PostgreSQL real.

## Objetivo del proyecto

Demostrar conocimientos prácticos en:
- arquitectura de microservicios,
- sistemas financieros distribuidos,
- infraestructura fintech,
- procesamiento de pagos,
- diseño de sistemas resilientes y auditables.

## Qué demuestra este proyecto

- Construcción de un ledger financiero real
- Integración con proveedores fintech
- Manejo seguro de balances y transacciones
- Diseño orientado a escalabilidad y consistencia
- Aplicación de patrones utilizados en fintechs modernas como Mercado Pago, Ualá o Pomelo
