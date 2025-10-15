# Proyecto Base Implementando Clean Architecture

Este proyecto implementa una **API reactiva** con **Spring Boot + WebFlux**, estructurada bajo el patrón **Clean Architecture**.

Repositorio en GitHub 👉 [anfega154/Asulado](https://github.com/anfega154/Asulado)

---


## 📖 Tabla de Contenido
- [Arquitectura](#arquitectura)
- [Módulos del Proyecto](#módulos-del-proyecto)
- [Domain](#domain)
  - [Usecases](#usecases)
- [Infrastructure](#infrastructure)
    - [Driven Adapters](#driven-adapters)
    - [Entry Points](#entry-points)
- [Persistencia](#persistencia)
- [Ejecución del Proyecto](#ejecución-del-proyecto)
- [Clonar Repositorio](#clonar-repositorio)
- [API REST](#api-rest)
- [Documentación Swagger](#documentación-swagger)
- [Pruebas](#pruebas)
- [SonnarQube](#sonnarqube)

---

##  Arquitectura

El proyecto está basado en **Clean Architecture**, siguiendo la plantilla de Bancolombia.  
Esto permite mantener el **dominio protegido**, separando la lógica de negocio de los detalles técnicos.

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

---

## Módulos del Proyecto

### Domain
- Contiene los **modelos del dominio** (`Pageable`, `ScheduledPayment`).
- Define las **interfaces de repositorio** (`ScheduledPaymentRepository`).
- Encapsula las **reglas de negocio**.
- Se exponen los casos de uso mediante puertos (interfaces). (`ScheduledPaymentInputPort`).
- No tiene dependencias hacia otros módulos.


### Usecases
- Implementa los **casos de uso** del sistema:
    - Listar pagos programados.

Los casos de uso **orquestan la lógica de aplicación** y son invocados por los entry points.

### Infrastructure

#### Driven Adapters
- Implementaciones técnicas:
    - Adaptador de persistencia con **Spring Data R2DBC** (`ScheduledPaymentRepositoryImpl`).
    - Configuración de la base de datos reactiva (R2DBC).
  

#### Entry Points
- **Routers + Handlers** basados en **Spring WebFlux**.
- Documentados con **springdoc-openapi** para Swagger UI.

### Application
- Módulo más externo de la arquitectura.
- Configura el arranque de Spring Boot (`MainApplication`).
- Ensambla los módulos, resuelve dependencias y expone los casos de uso como beans.

---


## Persistencia
- El proyecto utiliza una base de datos **reactiva** con **R2DBC**.
- Configuración en `application.yml` para conexión a PostgreSQL.
- Utliza liquidbase para la gestión de migraciones de esquema.
- Esquema de ejemplo en `db/changelog/db.changelog-master.yaml`.

-----

## Ejecución del Proyecto
-Requisitos:
- Java 21+
- Maven 8+
- PostgreSQL 12+

-Pasos:
# Clonar repositorio
git clone https://github.com/anfega154/Asulado.git
cd Asulado
- La aplicación corre en http://localhost:8080

# Compilar
./gradlew clean build

# Ejecutar
./gradlew bootRun

# crear contenedores de docker
cd deployment
docker-compose up -d --build

- La pgAdmin correrá en 👉 http://localhost:5052

---
## API REST
| Método   | Endpoint                   | Descripción                                         |
|----------|----------------------------|-----------------------------------------------------|
| GET      | api/v1/scheduled-payments  | Obtiene los pagos programados con filtros dinámicos |
| -------- |----------------------------|-----------------------------------------------------|

### Documentación Swagger
- La API está documentada con Swagger UI.
- Acceso en 👉 http://localhost:8080/swagger-ui.html
- OpenAPI JSON: 👉 http://localhost:8080/v3/api-docs

----

## Pruebas
- El proyecto incluye pruebas unitarias
- Frameworks: JUnit 5, Mockito
- Ejecutar pruebas:
```bash
./gradlew test
```
- Reportes de cobertura con JaCoCo:
```bash
./gradlew jacocoTestReport
```
- Reportes en: `build/reports/jacoco/test/html/index.html`

---

## SonnarQube
- Análisis de calidad de código con SonarQube.
- El sonnar corre en http://localhost:9000 (usuario: admin, contraseña: admin)
- Crear un token en SonarQube para el proyecto.
- En el proyecto, configurar sonar.properties con el token generado en SonarQube.
- Imagenes de sonnar:
- https://drive.google.com/file/d/1IeepnCy2MbKRzsWcPfaDeDZjdjXxW492/view?usp=drive_link
- https://drive.google.com/file/d/1eIhHQ4mcXaigZ--R83LvQrLA2ic4Bj5D/view?usp=drive_link
```bash
./gradlew clean build sonar
```
----