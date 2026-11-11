package com.techforb.app.modules.quotes.application;

import com.techforb.app.modules.quotes.application.dto.QuoteRequest;
import com.techforb.app.modules.quotes.application.dto.QuoteResponse;
import com.techforb.app.modules.quotes.application.dto.VehicleCatalogResponse;
import com.techforb.app.modules.quotes.domain.Quote;
import com.techforb.app.modules.quotes.domain.QuoteData;
import com.techforb.app.modules.quotes.domain.QuoteRepository;
import com.techforb.app.modules.quotes.domain.VehicleCatalogRepository;
import com.techforb.app.modules.quotes.domain.VehicleOption;
import com.techforb.app.shared.error.BusinessRuleException;
import com.techforb.app.shared.error.NotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

    private final QuoteRepository repository;
    private final VehicleCatalogRepository catalog;
    private final Clock clock;

    public QuoteService(QuoteRepository repository, VehicleCatalogRepository catalog, Clock clock) {
        this.repository = repository;
        this.catalog = catalog;
        this.clock = clock;
    }

    public VehicleCatalogResponse getVehicleCatalog() {
        // Catálogo dummy en memoria; reemplazar por datos de valuación vigentes.
        return VehicleCatalogResponse.from(catalog.findAll());
    }

    public QuoteResponse createQuote(QuoteRequest request) {
        VehicleOption vehicle = validateAndFindVehicle(request);
        Quote quote = new Quote(UUID.randomUUID().toString(), toData(request), vehicle, Instant.now(clock), LocalDate.now(clock));
        // Cotización dummy calculada y guardada en memoria; reemplazar al conectar datos reales.
        return QuoteResponse.from(repository.save(quote));
    }

    public QuoteResponse updateQuote(String id, QuoteRequest request) {
        Quote previous = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("No encontramos una cotización con esos datos"));
        if (previous.isExpired(LocalDate.now(clock))) {
            throw new BusinessRuleException("QUOTE_EXPIRED", "La cotización venció. Realizá una nueva cotización");
        }
        if (!previous.getData().dni().equals(request.dni())) {
            throw new BusinessRuleException("QUOTE_DNI_MISMATCH", "El DNI no coincide con el de la cotización guardada.");
        }
        VehicleOption vehicle = validateAndFindVehicle(request);
        // Se crea una nueva versión antes de guardarla: ante un error se conserva la anterior.
        Quote updated = previous.recalculate(toData(request), vehicle, Instant.now(clock));
        return QuoteResponse.from(repository.save(updated));
    }

    public QuoteResponse recoverQuote(String id, String dni) {
        Quote quote = repository.findById(id)
                .filter(found -> found.getData().dni().equals(dni))
                .orElseThrow(() -> new NotFoundException("No encontramos una cotización con esos datos"));
        if (quote.isExpired(LocalDate.now(clock))) {
            throw new BusinessRuleException("QUOTE_EXPIRED", "La cotización venció. Realizá una nueva cotización");
        }
        return QuoteResponse.from(quote);
    }

    private VehicleOption validateAndFindVehicle(QuoteRequest request) {
        LocalDate today = LocalDate.now(clock);
        if (request.year() > today.getYear()) {
            throw new BusinessRuleException("INVALID_VEHICLE_YEAR", "El año del vehículo no puede ser posterior al actual.");
        }
        if (!request.birthDate().isBefore(today)) {
            throw new BusinessRuleException("INVALID_BIRTH_DATE", "La fecha de nacimiento debe ser anterior a hoy.");
        }
        if (request.yearsLicensed() > Period.between(request.birthDate(), today).getYears()) {
            throw new BusinessRuleException("INVALID_LICENSE_YEARS", "Los años de registro no pueden superar los años transcurridos desde el nacimiento.");
        }
        return catalog.find(request.brand(), request.model(), request.version(), request.year())
                .orElseThrow(() -> new BusinessRuleException("VEHICLE_NOT_AVAILABLE", "No hay vehículos disponibles para esta selección"));
    }

    private QuoteData toData(QuoteRequest request) {
        return new QuoteData(request.dni(), request.brand(), request.model(), request.version(), request.year(),
                request.vehicleUse(), request.hasCng(), request.postalCode(), request.birthDate(),
                request.yearsLicensed(), request.claimsLastThreeYears(), request.paymentMethod());
    }
}
