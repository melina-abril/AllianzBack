package com.techforb.app.modules.quotes.api;

import com.techforb.app.modules.quotes.application.QuoteService;
import com.techforb.app.modules.quotes.application.dto.QuoteRequest;
import com.techforb.app.modules.quotes.application.dto.QuoteResponse;
import com.techforb.app.modules.quotes.application.dto.VehicleCatalogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Recibe las solicitudes HTTP y delega las reglas de cotización en el servicio. */
@RestController
@RequestMapping("/api/quotes")
@Tag(name = "Cotizaciones")
public class QuoteController {

    private final QuoteService service;

    public QuoteController(QuoteService service) {
        this.service = service;
    }

    @GetMapping("/catalog")
    @Operation(summary = "Catálogo de vehículos disponibles para cotizar")
    public VehicleCatalogResponse getVehicleCatalog() {
        return service.getVehicleCatalog();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Calcula y guarda una cotización")
    public QuoteResponse createQuote(@Valid @RequestBody QuoteRequest request) {
        return service.createQuote(request);
    }

    @PutMapping("/{number}")
    @Operation(summary = "Recalcula una cotización vigente sin extender su vigencia")
    public QuoteResponse updateQuote(@PathVariable String number, @Valid @RequestBody QuoteRequest request) {
        return service.updateQuote(number, request);
    }

    @GetMapping("/{number}")
    @Operation(summary = "Recupera una cotización vigente por número y DNI")
    public QuoteResponse recoverQuote(@PathVariable String number, @RequestParam(required = false) String dni) {
        return service.recoverQuote(number, dni);
    }
}
