# Spring Cloud - Banco XYZ

**Grupo 17** — Semana 6: Microservicios y seguridad con Spring Cloud
**Semana 7**: Tolerancia a fallos y arquitectura de eventos (Kafka)
**Semana 8** (individual): OAuth2.0, Docker y docker-compose
**Semana 9** (evaluación final): microservicios de Clientes y Pagos, eventos de pagos y preparación para AWS

## Objetivo del proyecto

Arquitectura de microservicios para Banco XYZ, lista para un entorno Cloud: resiliente, orientada a eventos, segura con OAuth2.0, y completamente dockerizada.

## Componentes

- **Config Server** (8888): configuración centralizada (un archivo `.properties` por servicio en `config-repo`).
- **Eureka Server** (8761): Service Discovery.
- **api-cuentas** (8081): gestión de cuentas — expone datos migrados de [bank_legacy_data](https://github.com/KariVillagran/bank_legacy_data), con Resilience4j (Circuit Breaker + Retry), productor de eventos Kafka y seguridad OAuth2.0 (Resource Server).
- **pagos-service** (8083): procesamiento de pagos, transferencias y depósitos. Valida las operaciones, las persiste y publica el evento `PAGO_COMPLETADO` en Kafka. Resilience4j y OAuth2.0.
- **clientes-service** (8084): gestión de clientes (CRUD completo, RUT único, validaciones en DTO). Resilience4j y OAuth2.0.
- **notificaciones-service** (8082): consumidor Kafka, procesa eventos de transacciones.
- **Keycloak** (8080): Authorization Server OAuth2.0 — emite y valida tokens.
- **Kafka** (9092) y **MySQL** (3307 en el host): infraestructura de datos y mensajería.

## Endpoints

Todos exigen `Authorization: Bearer <access_token>`, excepto `/actuator/**`.

| Servicio | Método y ruta | Respuestas |
|---|---|---|
| api-cuentas | `GET /api/cuentas`, `GET /api/cuentas/{id}` | 200, 404 |
| clientes-service | `GET /api/clientes`, `GET /api/clientes/{id}` | 200, 404 |
| clientes-service | `POST /api/clientes`, `PUT /api/clientes/{id}`, `DELETE /api/clientes/{id}` | 201 / 200 / 204, 400, 404, 409 (RUT duplicado) |
| pagos-service | `GET /api/pagos`, `GET /api/pagos/{id}` | 200, 404 |
| pagos-service | `POST /api/pagos` (tipo `PAGO`, `TRANSFERENCIA` o `DEPOSITO`) | 201, 400 |

Ejemplo de pago:
```json
{ "cuentaOrigenId": 1, "cuentaDestinoId": 2, "tipo": "TRANSFERENCIA", "monto": 15000 }
```

## Arquitectura de eventos (Kafka)

Patrón **Publish/Subscribe** sobre el tópico `transacciones-eventos`:

- `api-cuentas` publica `CONSULTA_CUENTA` cada vez que se consulta una cuenta.
- `pagos-service` publica `PAGO_COMPLETADO` cada vez que se procesa un pago.
- `notificaciones-service` consume ambos eventos y los procesa.

Si Kafka no está disponible, el pago se guarda igual y el fallo de publicación solo queda registrado en el log (el productor no bloquea la operación).

## Seguridad: OAuth2.0 con Keycloak

`api-cuentas`, `clientes-service` y `pagos-service` actúan como **Resource Server OAuth2.0**: validan los JWT emitidos por Keycloak (realm `banco-xyz`, client `api-cuentas-client`) contra su `issuer-uri`, sin manejar la autenticación directamente.

**Obtener un token (grant `client_credentials`):**
```
POST http://localhost:8080/realms/banco-xyz/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials
client_id=api-cuentas-client
client_secret=<secret del client en Keycloak>
```

**Usar el token:**
```
GET http://localhost:8081/api/cuentas
Authorization: Bearer <access_token>
```

## Dockerización

Cada microservicio tiene su propio `Dockerfile` (multi-stage: build con Maven, imagen final solo con el JRE + jar). Todo el stack se orquesta con `docker-compose.yaml`.

## Instrucciones para ejecutar el proyecto

### Opción A: con Docker (recomendado)

1. Crea un archivo `.env` en la raíz con:
```
   MYSQL_ROOT_PASSWORD=<tu-password>
```
2. Levanta todo:
```bash
   docker compose up --build
```
   Esto levanta: MySQL, Kafka, Keycloak, config-server, eureka-server, api-cuentas, pagos-service, clientes-service y notificaciones-service.
3. Configura el realm/client en Keycloak (`http://localhost:8080`) como se describe en la sección de seguridad.
4. Verifica en Eureka (`http://localhost:8761`) que los cuatro microservicios aparecen registrados.

Las tablas `clientes` y `pagos` se crean automáticamente al primer arranque (`ddl-auto=update`). `api-cuentas` lee la tabla `annual_statement_summary`, generada por el proyecto batch.

### Opción B: local (sin Docker)

Requiere Java 17+, Maven, MySQL/MariaDB y Kafka instalados localmente. Levantar en orden: Kafka → Config Server → Eureka Server → api-cuentas / pagos-service / clientes-service / notificaciones-service, cada uno con `./mvnw spring-boot:run`.

## Patrones de resiliencia implementados

Aplicados en `api-cuentas`, `clientes-service` y `pagos-service` (instancias `cuentaService`, `clienteService` y `pagoService`):

- **Retry**: hasta 3 reintentos ante fallas transitorias de base de datos.
- **Circuit Breaker**: se abre si el 50%+ de las últimas 5 llamadas fallan, devolviendo un fallback (lista vacía).

## Manejo de errores

`@RestControllerAdvice` (`GlobalExceptionHandler`) centraliza las excepciones en cada microservicio, devolviendo `ErrorResponse` estructurado: 404 (no encontrado), 400 (validación o pago inválido), 409 (cliente duplicado) y 500 (error interno).

## Repositorios relacionados

- Migración batch (Spring Batch): https://github.com/Catalina-cp/bank_legacymigration
- Patrón BFF: https://github.com/Catalina-cp/bff_banco_xyz

## Autores

- Catalina Cabezas