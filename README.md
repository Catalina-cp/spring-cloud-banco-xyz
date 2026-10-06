# Spring Cloud - Banco XYZ

**Grupo 17** — Semana 6: Microservicios y seguridad con Spring Cloud
**Semana 7**: Tolerancia a fallos y arquitectura de eventos (Kafka)
**Semana 8** (individual): OAuth2.0, Docker y docker-compose

## Objetivo del proyecto

Arquitectura de microservicios para Banco XYZ, lista para un entorno Cloud: resiliente, orientada a eventos, segura con OAuth2.0, y completamente dockerizada.

## Componentes

- **Config Server** (8888): configuración centralizada.
- **Eureka Server** (8761): Service Discovery.
- **api-cuentas** (8081): microservicio de negocio — expone datos migrados de [bank_legacy_data](https://github.com/KariVillagran/bank_legacy_data), con Resilience4j (Circuit Breaker + Retry), productor de eventos Kafka, y seguridad OAuth2.0 (Resource Server).
- **notificaciones-service** (8082): consumidor Kafka, procesa eventos de transacciones.
- **Keycloak** (8080): Authorization Server OAuth2.0 — emite y valida tokens.
- **Kafka** (9092) y **MySQL** (3307 en el host): infraestructura de datos y mensajería.

## Arquitectura de eventos (Kafka)

Patrón **Publish/Subscribe**: `api-cuentas` publica un evento `TransaccionEvento` en el tópico `transacciones-eventos` cada vez que se consulta una cuenta; `notificaciones-service` lo consume y procesa.

## Seguridad: OAuth2.0 con Keycloak

`api-cuentas` actúa como **Resource Server OAuth2.0**: valida los JWT emitidos por Keycloak (realm `banco-xyz`, client `api-cuentas-client`) contra su `issuer-uri`, sin manejar la autenticación directamente.

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
   Esto levanta: MySQL, Kafka, Keycloak, config-server, eureka-server, api-cuentas y notificaciones-service.
3. Configura el realm/client en Keycloak (`http://localhost:8080`) como se describe en la sección de seguridad.

### Opción B: local (sin Docker)

Requiere Java 17+, Maven, MySQL/MariaDB y Kafka instalados localmente. Levantar en orden: Kafka → Config Server → Eureka Server → api-cuentas → notificaciones-service, cada uno con `./mvnw spring-boot:run`.

## Patrones de resiliencia implementados

- **Retry**: hasta 3 reintentos ante fallas transitorias de base de datos.
- **Circuit Breaker**: se abre si el 50%+ de las últimas 5 llamadas fallan, devolviendo un fallback (lista vacía).

## Manejo de errores

`@RestControllerAdvice` (`GlobalExceptionHandler`) centraliza las excepciones, devolviendo `ErrorResponse` estructurado: 404 (no encontrado), 400 (validación), 500 (error interno).

## Autores

