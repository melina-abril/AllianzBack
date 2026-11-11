package com.techforb.app.modules.quotes.api;

import com.techforb.app.modules.quotes.application.QuoteService;
import com.techforb.app.modules.quotes.application.dto.QuoteRequest;
import com.techforb.app.modules.quotes.application.dto.QuoteResponse;
import com.techforb.app.modules.quotes.application.dto.VehicleCatalogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/quotes")
@Tag(name = "Cotizaciones")
public class QuoteController {

    private final QuoteService service;

    public QuoteController(QuoteService service) {
        this.service = service;
    }

    @GetMapping("/vehicle-catalog")
    @Operation(summary = "Catálogo de vehículos disponibles para cotizar")
    public VehicleCatalogResponse getVehicleCatalog() {
        return service.getVehicleCatalog();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Calcula y guarda una cotización por 15 días")
    public QuoteResponse createQuote(@Valid @RequestBody QuoteRequest request) {
        return service.createQuote(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una cotización sin extender su vigencia")
    public QuoteResponse updateQuote(@PathVariable String id, @Valid @RequestBody QuoteRequest request) {
        return service.updateQuote(id, request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recupera una cotización vigente por número y DNI")
    public QuoteResponse recoverQuote(
            @PathVariable String id,
            @RequestParam @Pattern(regexp = "^[0-9]{7,8}$", message = "El DNI debe tener siete u ocho dígitos.") String dni) {
        return service.recoverQuote(id, dni);
    }
}
