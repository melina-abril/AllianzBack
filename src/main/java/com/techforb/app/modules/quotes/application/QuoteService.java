package com.techforb.app.modules.quotes.application;

import com.techforb.app.modules.quotes.application.dto.QuoteRequest;
import com.techforb.app.modules.quotes.application.dto.QuoteResponse;
import com.techforb.app.modules.quotes.application.dto.VehicleResponse;
import com.techforb.app.modules.quotes.domain.Quote;
import com.techforb.app.modules.quotes.domain.QuoteInput;
import com.techforb.app.modules.quotes.domain.QuoteRepository;
import com.techforb.app.modules.quotes.domain.Vehicle;
import com.techforb.app.shared.error.BusinessRuleException;
import com.techforb.app.shared.error.NotFoundException;
import com.techforb.app.shared.web.PageResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

    private final QuoteRepository repository;
    private final Clock clock;

    public QuoteService(QuoteRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public PageResponse<VehicleResponse> listQuotableVehicles(
            String brand, String model, String version, Integer year, int page, int size) {
        List<Vehicle> vehicles = repository.findAllVehicles().stream()
                .filter(vehicle -> matches(vehicle.brand(), brand))
                .filter(vehicle -> matches(vehicle.model(), model))
                .filter(vehicle -> matches(vehicle.version(), version))
                .filter(vehicle -> year == null || vehicle.year() == year)
                .filter(vehicle -> vehicle.year() <= LocalDate.now(clock).getYear())
                .toList();
        return PageResponse.of(vehicles, page, size, VehicleResponse::from);
    }

    public QuoteResponse createQuote(QuoteRequest request) {
        QuoteInput input = validateAndBuild(request);
        Instant now = Instant.now(clock);
        LocalDate today = LocalDate.now(clock);
        String number = "COT-" + UUID.randomUUID().toString().toUpperCase(Locale.ROOT);
        Quote quote = new Quote(number, input, now, today.plusDays(15), today);
        // Respuesta dummy respaldada por datos en memoria; reemplazar al conectar el almacenamiento real.
        return QuoteResponse.from(repository.save(quote));
    }

    public QuoteResponse updateQuote(String id, QuoteRequest request) {
        Quote quote = findMatching(id, request.dni());
        assertCurrent(quote);
        QuoteInput input = validateAndBuild(request);
        // Se calcula completamente antes de modificar lo guardado; ante un error permanece la versión anterior.
        quote.recalculate(input, Instant.now(clock), LocalDate.now(clock));
        return QuoteResponse.from(repository.save(quote));
    }

    public QuoteResponse retrieveQuote(String id, String dni) {
        Quote quote = findMatching(id, dni);
        assertCurrent(quote);
        return QuoteResponse.from(quote);
    }

    private Quote findMatching(String id, String dni) {
        return repository.findById(id)
                .filter(quote -> quote.getInput().dni().equals(dni))
                .orElseThrow(() -> new NotFoundException("No encontramos una cotización con esos datos"));
    }

    private void assertCurrent(Quote quote) {
        if (LocalDate.now(clock).isAfter(quote.getExpiresOn())) {
            throw new BusinessRuleException("QUOTE_EXPIRED", "La cotización venció. Realizá una nueva cotización");
        }
    }

    private QuoteInput validateAndBuild(QuoteRequest request) {
        LocalDate today = LocalDate.now(clock);
        if (request.year() > today.getYear()) {
            throw new BusinessRuleException("INVALID_VEHICLE_YEAR", "El año del vehículo no puede ser posterior al actual.");
        }
        if (request.birthDate() == null || !request.birthDate().isBefore(today)) {
            throw new BusinessRuleException("INVALID_BIRTH_DATE", "La fecha de nacimiento debe ser anterior a hoy.");
        }
        if (request.registrationYears() == null || request.registrationYears() < 0
                || request.claimsLastThreeYears() == null || request.claimsLastThreeYears() < 0) {
            throw new BusinessRuleException("INVALID_DRIVER_DATA", "Revisá los datos indicados para cotizar");
        }
        if (request.registrationYears() > Period.between(request.birthDate(), today).getYears()) {
            throw new BusinessRuleException("INVALID_REGISTRATION_YEARS", "Los años de registro no pueden superar la edad del conductor.");
        }
        Vehicle vehicle = repository.findAllVehicles().stream()
                .filter(candidate -> candidate.brand().equalsIgnoreCase(request.brand().trim()))
                .filter(candidate -> candidate.model().equalsIgnoreCase(request.model().trim()))
                .filter(candidate -> candidate.version().equalsIgnoreCase(request.version().trim()))
                .filter(candidate -> candidate.year() == request.year())
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("VEHICLE_NOT_AVAILABLE",
                        "No hay vehículos disponibles para esta selección"));
        return new QuoteInput(vehicle, request.use(), request.gnc(), request.postalCode(), request.birthDate(),
                request.registrationYears(), request.claimsLastThreeYears(), request.paymentMethod(), request.dni());
    }

    private boolean matches(String value, String filter) {
        return filter == null || filter.isBlank()
                || value.toLowerCase(Locale.ROOT).contains(filter.trim().toLowerCase(Locale.ROOT));
    }
}
