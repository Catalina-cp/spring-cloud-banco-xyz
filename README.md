# Spring Cloud - Banco XYZ

**Grupo 17** — Actividad Semana 6: Implementando microservicios y seguridad en la nube con Spring Cloud

## Objetivo del proyecto

Implementar una arquitectura de microservicios resiliente y segura utilizando Spring Cloud, compuesta por:
- Un **Config Server** centralizado para la configuración.
- Un **Service Discovery** (Eureka) para el registro de microservicios.
- Un **microservicio (`api-cuentas`)** que expone los datos resultantes de la migración del proyecto batch de Banco XYZ (basado en [bank_legacy_data](https://github.com/KariVillagran/bank_legacy_data)), con autenticación JWT y tolerancia a fallos (Circuit Breaker + Retry).

## Estructura del proyecto