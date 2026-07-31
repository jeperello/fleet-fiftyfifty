# Fleet-fiftyfifty — Módulo: Consolidación de Ingresos Semanales

Versión: 1.0 (Draft)

Este repositorio contiene un microservicio Spring Boot (Java 21, Spring Boot 4.1.0) que implementa el módulo de "Consolidación de Ingresos Semanales" para una flota compartida entre Propietario y Conductor.

Resumen rápido
- Objetivo: centralizar, clasificar y calcular la distribución de dinero recaudado por la flota y determinar el finiquito entre las partes (quién debe transferir a quién y cuánto).
- Lenguaje: Java 21
- Framework: Spring Boot 4.1.0
- Build: Maven (incluye `mvnw` / `mvnw.cmd`)

Checklist de lo que contiene este README
- Visión general y reglas de negocio
- Mapeo del Lenguaje Ubicuo (DDD) a clases del código
- Instrucciones para compilar, ejecutar y probar
- Escenarios de aceptación y cómo ejecutarlos localmente

1) Visión general y reglas principales

Reglas clave implementadas (resumen):
- Precisión financiera invariable: BigDecimal con 2 decimales y redondeo HALF_UP.
- ProfitSplit configurable (por defecto 50/50). El porcentaje definido para el OWNER se complementa automáticamente para el DRIVER.
- Cálculo de TargetShare (cuota justa):
  - TargetShare(OWNER) = GrandTotal * (OwnerPercentage / 100)
  - TargetShare(DRIVER) = GrandTotal - TargetShare(OWNER)
- Determinación de Deudor/Acreedor:
  - Si lo recaudado por el conductor > TargetShare(conductor) entonces el conductor es Deudor y debe transferir la diferencia al propietario.

2) Mapeo del Lenguaje Ubicuo a código (archivos principales)

El proyecto ya contiene el modelo de dominio relacionado. Aquí se indican las clases/paquetes relevantes (rutas dentro de `src/main/java`):

- Aggregate root
  - `com.jeperello.fleetfiftyfifty.domain.WeeklyConsolidation` — Raíz del agregado que agrupa incomes, aplica reglas y calcula finiquitos.

- Value Objects / Entidades del dominio
  - `com.jeperello.fleetfiftyfifty.domain.Money` — Representa montos con BigDecimal y reglas de redondeo.
  - `com.jeperello.fleetfiftyfifty.domain.Income` — Registro de un ingreso (role, platform, paymentMethod, amount).
  - `com.jeperello.fleetfiftyfifty.domain.ProfitSplit` — Porcentaje pactado para OWNER (y complementario para DRIVER).
  - `com.jeperello.fleetfiftyfifty.domain.BalanceResult` — Resultado del finiquito (deudor, acreedor, amount).
  - `com.jeperello.fleetfiftyfifty.domain.FleetRole` — Enum OWNER / DRIVER.
  - `com.jeperello.fleetfiftyfifty.domain.Platform` — Enum DIDI / UBER.
  - `com.jeperello.fleetfiftyfifty.domain.PaymentMethod` — Enum CASH / BANK_TRANSFER / MERCADO_PAGO.

- DTOs de aplicación
  - `com.jeperello.fleetfiftyfifty.application.dto.*` — Comandos y respuestas para uso en tests o controladores.

3) Compilar y ejecutar

Requisitos previos
- JDK 21 instalado y disponible en PATH.
- Maven o usar los wrappers incluidos (`mvnw`, `mvnw.cmd`).

Compilación
En PowerShell (Windows):

```powershell
.\mvnw.cmd -DskipTests package
```

Ejecutar la aplicación (desarrollo)

```powershell
.\mvnw.cmd spring-boot:run
```

Ejecutar tests

```powershell
.\mvnw.cmd test
```

Nota: también se puede ejecutar con `mvn` si se prefiere.

4) Cómo usar localmente / Casos de prueba de aceptación

El proyecto ya incluye pruebas unitarias orientadas al agregado `WeeklyConsolidation`. Para validar los escenarios descritos en la especificación, ejecuta los tests:

```powershell
.\mvnw.cmd test
```

Escenario de ejemplo (Caso Base 1 — 50/50)
- Ingresos (formato: Plataforma | Rol | Método | Monto):
  - DiDi | OWNER | BANK_TRANSFER | 2,000.00
  - DiDi | DRIVER | CASH | 50,000.00
  - DiDi | DRIVER | MERCADO_PAGO | 26,000.00
  - Uber | DRIVER | CASH | 27,000.00
  - Uber | OWNER | BANK_TRANSFER | 36,000.00

Resultados esperados (resumen):
- Subtotal DiDi: 78,000.00
- Subtotal Uber: 63,000.00
- Recaudado OWNER: 38,000.00
- Recaudado DRIVER: 103,000.00
- GrandTotal: 141,000.00
- Target (50/50): 70,500.00 cada uno
- Finiquito: DRIVER es Deudor y debe transferir 32,500.00 al OWNER

Escenario de ejemplo (Caso Base 2 — 60/40)
- ProfitSplit: OWNER 60% / DRIVER 40% (mismos ingresos)
- Target OWNER: 84,600.00
- Target DRIVER: 56,400.00
- Finiquito: DRIVER debe transferir 46,600.00 al OWNER

5) Extensiones y notas de diseño
- El diseño actual sigue un enfoque DDD ligero: las clases en `domain` representan el lenguaje ubicuo descrito en la especificación.
- Principio YAGNI aplicado: los ingresos no contienen campos libres (notas/descripcion) en esta versión.
- Las reglas financieras críticas (BigDecimal con 2 decimales y HALF_UP) están centralizadas en `Money`.

6) Contribuir / Ejecutar pruebas adicionales
- Para añadir un nuevo escenario de aceptación, crea un test en `src/test/java/...` que construya un `WeeklyConsolidation`, agregue `Income` según el escenario y verifique el `BalanceResult`.

Comandos útiles (PowerShell):

```powershell
# Ejecutar tests
.\mvnw.cmd test

# Compilar sin tests
.\mvnw.cmd -DskipTests package

# Ejecutar la app
.\mvnw.cmd spring-boot:run
```

Contacto / Autor
- Proyecto creado por el equipo de desarrollo (meta: `com.jeperello`).

Licencia
- No se especificó licencia en el `pom.xml`; añadir un archivo `LICENSE` si el proyecto debe ser abierto/compartido.

----
Este README resume la especificación funcional y facilita ejecutar y verificar los casos de aceptación incluidos en el repositorio.

