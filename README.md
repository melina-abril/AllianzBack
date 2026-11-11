# Allianz — API de seguros de autos

Backend REST desarrollado con Java 21 y Spring Boot 4.1. Los datos se almacenan en memoria y se reinician al volver a levantar la aplicación; las respuestas de ejemplo son provisionales.

## Ejecutar

Necesitas Java 21 y Maven. Desde la raíz del proyecto:

```bash
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080/api` y la documentación interactiva en `http://localhost:8080/swagger-ui.html`.

## Módulos y endpoints

### Cotizador de seguro de auto (`quotes`)

| Método | Endpoint | Descripción |
| --- | --- | --- |
| GET | `/api/quotes/vehicle-catalog` | Consultar marcas, modelos, versiones y años disponibles. |
| POST | `/api/quotes` | Crear una cotización con cuatro coberturas. |
| PUT | `/api/quotes/{id}` | Actualizar una cotización vigente. |
| GET | `/api/quotes/{id}` | Recuperar una cotización vigente; requiere el DNI como parámetro de consulta. |

### Asegurados y vehículos (`policyholders`)

| Método | Endpoint | Descripción |
| --- | --- | --- |
| GET | `/api/policyholders` | Listar asegurados con paginación y filtros opcionales. |
| GET | `/api/policyholders/{id}` | Consultar la ficha de un asegurado. |
| POST | `/api/policyholders` | Registrar un asegurado. |
| PUT | `/api/policyholders/{id}` | Actualizar un asegurado. |
| DELETE | `/api/policyholders/{id}` | Dar de baja a un asegurado sin pólizas vigentes. |
| POST | `/api/vehicles` | Registrar un vehículo. |
| GET | `/api/vehicles/{id}` | Consultar un vehículo. |
| PUT | `/api/vehicles/{id}` | Actualizar un vehículo. |

Los listados aceptan `page` y `size` como parámetros de consulta; por defecto, la convención es `page=0&size=10`. Los controladores de ambos módulos se registran automáticamente mediante el escaneo de componentes de Spring Boot.
