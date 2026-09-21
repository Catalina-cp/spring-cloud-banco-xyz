# Spring Cloud - Banco XYZ

**Grupo 17** — Actividad Semana 6: Implementando microservicios y seguridad en la nube con Spring Cloud

## Objetivo del proyecto

Implementar una arquitectura de microservicios resiliente y segura utilizando Spring Cloud, compuesta por:
- Un **Config Server** centralizado para la configuración.
- Un **Service Discovery** (Eureka) para el registro de microservicios.
- Un **microservicio (`api-cuentas`)** que expone los datos resultantes de la migración del proyecto batch de Banco XYZ (basado en [bank_legacy_data](https://github.com/KariVillagran/bank_legacy_data)), con autenticación JWT y tolerancia a fallos (Circuit Breaker + Retry).

## Estructura del proyecto

```
spring-cloud-banco-xyz/
├── config-server/    # Servidor de configuración centralizada (puerto 8888)
├── eureka-server/    # Servidor de Service Discovery (puerto 8761)
└── api-cuentas/      # Microservicio de negocio (puerto 8081)
```

### Detalle de `api-cuentas`
- **model/**: entidad JPA `AnnualStatementSummary`, mapeada a la tabla `annual_statement_summary`.
- **repository/**: `AnnualStatementSummaryRepository` (Spring Data JPA).
- **service/**: `CuentaService`, con lógica de negocio y anotaciones de Resilience4j.
- **controller/**: `CuentaController` (endpoints de datos) y `AuthController` (generación de JWT).
- **security/**: `JwtUtil`, `JwtAuthFilter`, `SecurityConfig` — autenticación basada en JWT.
- **exception/**: `GlobalExceptionHandler` (`@RestControllerAdvice`), `CuentaNoEncontradaException`, `ErrorResponse` — manejo centralizado de errores.

## Requisitos previos

- Java 17+
- Maven (o usar los wrappers `mvnw` incluidos en cada proyecto)
- MySQL/MariaDB corriendo localmente, con la base de datos `bank_batch` ya poblada (resultado del proyecto de migración batch previo)

## Instrucciones para ejecutar el proyecto

Los 3 servicios deben levantarse **en este orden**, cada uno en su propia terminal:

### 1. Config Server
```bash
cd config-server
./mvnw spring-boot:run
```
Verificar en `http://localhost:8888/api-cuentas/default` que devuelve la configuración correctamente.

### 2. Eureka Server
```bash
cd eureka-server
./mvnw spring-boot:run
```
Verificar en `http://localhost:8761` que el dashboard carga correctamente.

### 3. api-cuentas
```bash
cd api-cuentas
./mvnw spring-boot:run
```
Verificar en `http://localhost:8761` que `API-CUENTAS` aparece registrado como instancia activa.

## Uso de la API

### 1. Generar un token JWT
```
POST http://localhost:8081/auth/token
Content-Type: application/json

{ "apiKey": "cuentas-secret-2026" }
```
Respuesta:
```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

### 2. Consultar todas las cuentas (requiere token)
```
GET http://localhost:8081/api/cuentas
Authorization: Bearer <token>
```

### 3. Consultar una cuenta específica
```
GET http://localhost:8081/api/cuentas/{cuentaId}
Authorization: Bearer <token>
```
Devuelve 404 con un cuerpo estructurado si la cuenta no existe.

## Patrones de resiliencia implementados

- **Retry**: hasta 3 reintentos ante fallas transitorias de conexión a la base de datos.
- **Circuit Breaker**: se abre si el 50% o más de las últimas 5 llamadas fallan, devolviendo una respuesta de respaldo (fallback: lista vacía) en lugar de seguir golpeando una base de datos caída, evitando saturarla.

## Manejo de errores

Todas las excepciones son capturadas de forma centralizada mediante `@RestControllerAdvice` (`GlobalExceptionHandler`), devolviendo respuestas estructuradas (`ErrorResponse`) con código de estado HTTP, mensaje descriptivo y timestamp:
- **404**: cuenta no encontrada
- **400**: errores de validación de datos de entrada
- **500**: errores internos inesperados
