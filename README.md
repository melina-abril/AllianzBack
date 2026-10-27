# Allianz — API de seguros de autos

Backend REST de ejemplo con Java 21 y Spring Boot 4.1. Los datos se mantienen en memoria; se reinician al volver a levantar la aplicación. Las respuestas actuales son datos de ejemplo que se reemplazarán al conectar la persistencia.

## Iniciar

Con Java 21 y Maven instalados, ejecuta `mvn spring-boot:run`. La API estará disponible en `http://localhost:8080/api` y la documentación interactiva en `http://localhost:8080/swagger-ui.html`. Para verificar la compilación, ejecuta `mvn compile`.

## Módulos y endpoints

### Cotizador de seguro de auto

| Método | Ruta | Función |
| --- | --- | --- |
| GET | `/api/quotes/catalog` | Consultar marcas, modelos, versiones y años disponibles. |
| POST | `/api/quotes` | Crear una cotización con cuatro coberturas. |
| PUT | `/api/quotes/{number}` | Actualizar y recalcular una cotización vigente. |
| GET | `/api/quotes/{number}` | Recuperar una cotización por número y DNI. |

### Asegurados y vehículos

| Método | Ruta | Función |
| --- | --- | --- |
| GET | `/api/policyholders` | Buscar asegurados con paginación (`page` y `size`). |
| GET | `/api/policyholders/{id}` | Consultar la ficha de un asegurado. |
| POST | `/api/policyholders` | Dar de alta un asegurado. |
| PUT | `/api/policyholders/{id}` | Editar un asegurado. |
| DELETE | `/api/policyholders/{id}` | Dar de baja un asegurado sin pólizas vigentes. |
| POST | `/api/policyholders/{id}/vehicles` | Agregar un vehículo. |
| PUT | `/api/policyholders/{id}/vehicles/{vehicleId}` | Editar un vehículo. |

Los errores se devuelven como `ProblemDetail`, con un código estable y, cuando corresponde, errores por campo. Consulta Swagger para ver los esquemas de solicitudes y respuestas.