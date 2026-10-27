# Allianz — API de seguros de autos

Backend REST con Java 21 y Spring Boot 4.1. Los datos se mantienen en memoria y las respuestas son de ejemplo; se pierden al reiniciar la aplicación.

## Iniciar

Con Java 21 y Maven instalados, ejecuta:

```sh
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080/api` y la documentación interactiva en `http://localhost:8080/swagger-ui.html`.

## Módulos y endpoints

### Cotizador de seguro de auto (`quotes`)

| Método | Endpoint | Descripción |
| --- | --- | --- |
| GET | `/api/quotes/vehicles` | Consulta las combinaciones de vehículos disponibles para cotizar. |
| POST | `/api/quotes` | Crea una cotización con cuatro coberturas y vigencia de 15 días. |
| PUT | `/api/quotes/{id}` | Recalcula una cotización vigente. |
| GET | `/api/quotes/{id}` | Recupera una cotización vigente; requiere el DNI como parámetro de consulta. |

### Asegurados y vehículos (`policyholders`)

| Método | Endpoint | Descripción |
| --- | --- | --- |
| GET | `/api/policyholders` | Busca asegurados con paginación. |
| GET | `/api/policyholders/{id}` | Consulta la ficha de un asegurado. |
| POST | `/api/policyholders` | Da de alta un asegurado. |
| PUT | `/api/policyholders/{id}` | Actualiza un asegurado. |
| POST | `/api/policyholders/{id}/vehicles` | Asocia un vehículo. |
| PUT | `/api/policyholders/{id}/vehicles/{vehicleId}` | Actualiza un vehículo asociado. |
| DELETE | `/api/policyholders/{id}` | Da de baja un asegurado sin pólizas vigentes. |

Spring Boot detecta automáticamente los controladores de ambos módulos bajo `com.techforb.app.modules`; no hace falta registrar rutas adicionales. Los errores se devuelven como `ProblemDetail` y los listados paginados incluyen `items`, `page`, `size`, `totalItems` y `totalPages`.
