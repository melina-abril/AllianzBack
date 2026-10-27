package com.techforb.app.modules.quotes.application;

import com.techforb.app.modules.quotes.application.dto.QuoteRequest;
import com.techforb.app.modules.quotes.application.dto.QuoteResponse;
import com.techforb.app.modules.quotes.application.dto.VehicleCatalogResponse;
import com.techforb.app.modules.quotes.domain.Quote;
import com.techforb.app.modules.quotes.domain.QuoteDetails;
import com.techforb.app.modules.quotes.domain.QuoteRepository;
import com.techforb.app.modules.quotes.domain.VehicleOption;
import com.techforb.app.shared.error.BusinessRuleException;
import com.techforb.app.shared.error.NotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.stereotype.Service;

/** Casos de uso y validaciones que dependen del catálogo o de la vigencia. */
@Service
public class QuoteService {

    private final QuoteRepository repository;
    private final Clock clock;

    public QuoteService(QuoteRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public VehicleCatalogResponse getVehicleCatalog() {
        return VehicleCatalogResponse.from(repository.findAllVehicles());
    }

    public QuoteResponse createQuote(QuoteRequest request) {
        VehicleOption vehicle = validateVehicle(request);
        Quote quote = Quote.create(UUID.randomUUID().toString(), details(request), vehicle,
                Instant.now(clock), LocalDate.now(clock).plusDays(15));
        // Respuesta dummy respaldada por datos en memoria; reemplazar al conectar la persistencia real.
        return QuoteResponse.from(repository.save(quote));
    }

    public QuoteResponse updateQuote(String number, QuoteRequest request) {
        Quote quote = find(number);
        assertCurrent(quote);
        if (!quote.getDetails().dni().equals(request.dni())) {
            throw new NotFoundException("No encontramos una cotización con esos datos");
        }
        VehicleOption vehicle = validateVehicle(request);
        quote.update(details(request), vehicle);
        return QuoteResponse.from(repository.save(quote));
    }

    public QuoteResponse recoverQuote(String number, String dni) {
        if (number == null || number.isBlank()) {
            throw new BusinessRuleException("QUOTE_NUMBER_REQUIRED", "Ingresá el número de cotización.");
        }
        if (dni == null || !dni.matches("[0-9]{7,8}")) {
            throw new BusinessRuleException("INVALID_DNI", "Ingresá un DNI de siete u ocho dígitos.");
        }
        Quote quote = repository.findByNumber(number)
                .filter(found -> found.getDetails().dni().equals(dni))
                .orElseThrow(() -> new NotFoundException("No encontramos una cotización con esos datos"));
        assertCurrent(quote);
        return QuoteResponse.from(quote);
    }

    private Quote find(String number) {
        return repository.findByNumber(number)
                .orElseThrow(() -> new NotFoundException("No encontramos una cotización con esos datos"));
    }

    private void assertCurrent(Quote quote) {
        if (LocalDate.now(clock).isAfter(quote.getExpiresOn())) {
            throw new BusinessRuleException("QUOTE_EXPIRED", "La cotización venció. Realizá una nueva cotización");
        }
    }

    private VehicleOption validateVehicle(QuoteRequest request) {
        LocalDate today = LocalDate.now(clock);
        if (request.year() > today.getYear()) {
            throw new BusinessRuleException("INVALID_VEHICLE_YEAR", "El año del vehículo no puede ser posterior al actual.");
        }
        if (!request.birthDate().isBefore(today)) {
            throw new BusinessRuleException("INVALID_BIRTH_DATE", "La fecha de nacimiento debe ser anterior a hoy.");
        }
        if (request.licenseYears() > ChronoUnit.YEARS.between(request.birthDate(), today)) {
            throw new BusinessRuleException("INVALID_LICENSE_YEARS", "Los años de registro no pueden superar los años transcurridos desde el nacimiento.");
        }
        return repository.findVehicle(request.brand(), request.model(), request.version(), request.year())
                .orElseThrow(() -> new BusinessRuleException("VEHICLE_NOT_AVAILABLE", "No hay vehículos disponibles para esta selección"));
    }

    private QuoteDetails details(QuoteRequest request) {
        return new QuoteDetails(request.brand(), request.model(), request.version(), request.year(),
                request.use(), request.gnc(), request.postalCode(), request.birthDate(),
                request.licenseYears(), request.claimsLastThreeYears(), request.paymentMethod(), request.dni());
    }
}
