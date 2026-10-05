# Spring Cloud - Banco XYZ

**Grupo 17** — Semana 6: Implementando microservicios y seguridad en la nube con Spring Cloud
**Semana 7**: Configurando tolerancia a fallos y arquitectura de eventos con microservicios en la nube

## Objetivo del proyecto

Implementar una arquitectura de microservicios resiliente, segura y orientada a eventos para Banco XYZ, compuesta por:
- Un **Config Server** centralizado para la configuración.
- Un **Service Discovery** (Eureka) para el registro de microservicios.
- Un **microservicio de negocio (`api-cuentas`)** que expone los datos resultantes de la migración del proyecto batch de Banco XYZ (basado en [bank_legacy_data](https://github.com/KariVillagran/bank_legacy_data)), con autenticación JWT y tolerancia a fallos (Circuit Breaker + Retry).
- Una **arquitectura de eventos asíncrona con Apache Kafka** (patrón Publish/Subscribe), donde `api-cuentas` publica eventos de transacciones y un nuevo microservicio (`notificaciones-service`) los consume y procesa.

## Arquitectura de eventos (Semana 7)

**Patrón elegido:** Publish/Subscribe con notificación de eventos, usando Apache Kafka.

**Tópico:** `transacciones-eventos`

**Flujo:**
1. Un cliente consulta una cuenta: `GET /api/cuentas/{id}` en `api-cuentas`.
2. `api-cuentas` (productor) publica un evento `TransaccionEvento` en el tópico `transacciones-eventos`.
3. `notificaciones-service` (consumidor), suscrito al tópico, recibe el evento y procesa la notificación (lo registra en consola, simulando el envío de una alerta al cliente).

**Estructura del evento:**
```json
{
  "cuentaId": 101,
  "tipoEvento": "CONSULTA_CUENTA",
  "timestamp": [2026, 10, 4, 18, 59, 0, 217836100]
}
```

## Estructura del proyecto

```
spring-cloud-banco-xyz/
├── config-server/           # Servidor de configuración centralizada (puerto 8888)
├── eureka-server/            # Servidor de Service Discovery (puerto 8761)
├── api-cuentas/               # Microservicio de negocio + productor Kafka (puerto 8081)
└── notificaciones-service/    # Microservicio consumidor Kafka (puerto 8082)
```

### Detalle de `api-cuentas`
- **model/**: entidad JPA `AnnualStatementSummary`, mapeada a la tabla `annual_statement_summary`.
- **repository/**: `AnnualStatementSummaryRepository` (Spring Data JPA).
- **service/**: `CuentaService`, con lógica de negocio y anotaciones de Resilience4j.
- **controller/**: `CuentaController` (endpoints de datos, publica eventos) y `AuthController` (generación de JWT).
- **security/**: `JwtUtil`, `JwtAuthFilter`, `SecurityConfig` — autenticación basada en JWT.
- **exception/**: `GlobalExceptionHandler` (`@RestControllerAdvice`), `CuentaNoEncontradaException`, `ErrorResponse` — manejo centralizado de errores.
- **event/**: `TransaccionEvento` (DTO del evento), `TransaccionEventProducer` (publica en Kafka vía `KafkaTemplate`).

### Detalle de `notificaciones-service`
- **event/**: `TransaccionEvento` (misma estructura que el productor, para deserializar el mensaje).
- **consumer/**: `TransaccionEventConsumer`, con un `@KafkaListener` que escucha el tópico `transacciones-eventos` y procesa cada evento recibido.

## Requisitos previos

- Java 17+
- Maven (o usar los wrappers `mvnw` incluidos en cada proyecto)
- MySQL/MariaDB corriendo localmente, con la base de datos `bank_batch` ya poblada
- Apache Kafka 4.x corriendo localmente en modo KRaft (standalone), en `localhost:9092`

## Instrucciones para ejecutar el proyecto

Deben levantarse **en este orden**, cada uno en su propia terminal:

### 1. Apache Kafka
```bash
cd <ruta-de-kafka>
bin/windows/kafka-server-start.bat config/server.properties
```
(En Linux/Mac: `bin/kafka-server-start.sh config/server.properties`)

### 2. Config Server
```bash
cd config-server
./mvnw spring-boot:run
```
Verificar en `http://localhost:8888/api-cuentas/default`.

### 3. Eureka Server
```bash
cd eureka-server
./mvnw spring-boot:run
```
Verificar en `http://localhost:8761`.

### 4. api-cuentas
```bash
cd api-cuentas
./mvnw spring-boot:run
```
Verificar en `http://localhost:8761` que `API-CUENTAS` aparece registrado.

### 5. notificaciones-service
```bash
cd notificaciones-service
./mvnw spring-boot:run
```
Verificar en `http://localhost:8761` que `NOTIFICACIONES-SERVICE` aparece registrado.

## Uso de la API

### 1. Generar un token JWT
```
POST http://localhost:8081/auth/token
Content-Type: application/json

{ "apiKey": "cuentas-secret-2026" }
```

### 2. Consultar una cuenta (dispara el evento Kafka)
```
GET http://localhost:8081/api/cuentas/{cuentaId}
Authorization: Bearer <token>
```
Al ejecutar esta petición, `notificaciones-service` debería mostrar en su consola:
```
=== NOTIFICACIÓN RECIBIDA ===
Cuenta ID: 101
Tipo de evento: CONSULTA_CUENTA
Procesando notificación para la cuenta 101...
==============================
```

### 3. Consultar todas las cuentas
```
GET http://localhost:8081/api/cuentas
Authorization: Bearer <token>
```

## Patrones de resiliencia implementados

- **Retry**: hasta 3 reintentos ante fallas transitorias de conexión a la base de datos.
- **Circuit Breaker**: se abre si el 50% o más de las últimas 5 llamadas fallan, devolviendo una respuesta de respaldo (fallback: lista vacía).

## Manejo de errores

Todas las excepciones son capturadas de forma centralizada mediante `@RestControllerAdvice` (`GlobalExceptionHandler`), devolviendo respuestas estructuradas (`ErrorResponse`) con código HTTP, mensaje y timestamp:
- **404**: cuenta no encontrada
- **400**: errores de validación
- **500**: errores internos inesperados

## Autores

Grupo 17