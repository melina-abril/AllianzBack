package com.techforb.app.modules.quotes.infrastructure;

import com.techforb.app.modules.quotes.domain.PaymentMethod;
import com.techforb.app.modules.quotes.domain.Quote;
import com.techforb.app.modules.quotes.domain.QuoteInput;
import com.techforb.app.modules.quotes.domain.QuoteRepository;
import com.techforb.app.modules.quotes.domain.Vehicle;
import com.techforb.app.modules.quotes.domain.VehicleUse;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** Datos DUMMY en memoria para el catálogo y las cotizaciones; reemplazar por persistencia real. */
@Repository
public class InMemoryQuoteRepository implements QuoteRepository {

    private final Map<String, Quote> quotes = new ConcurrentHashMap<>();
    private final List<Vehicle> vehicles;

    public InMemoryQuoteRepository(Clock clock) {
        int year = LocalDate.now(clock).getYear();
        vehicles = List.of(
                new Vehicle("veh-1001", "Toyota", "Corolla", "XEI 2.0 CVT", year - 2, new BigDecimal("24500000.00")),
                new Vehicle("veh-1002", "Toyota", "Corolla", "SEG 2.0 CVT", year - 1, new BigDecimal("29400000.00")),
                new Vehicle("veh-1003", "Volkswagen", "Polo", "Highline 170 TSI", year - 3, new BigDecimal("19800000.00")),
                new Vehicle("veh-1004", "Fiat", "Cronos", "Drive 1.3", year - 4, new BigDecimal("16750000.00")),
                new Vehicle("veh-1005", "Renault", "Sandero", "Intens 1.6", year - 6, new BigDecimal("14500000.00")));

        LocalDate today = LocalDate.now(clock);
        Instant now = Instant.now(clock);
        save(new Quote("COT-1001", new QuoteInput(vehicles.get(0), VehicleUse.PRIVATE, false,
                "1425", today.minusYears(36), 14, 0, PaymentMethod.AUTOMATIC_DEBIT, "30123456"),
                now.minusSeconds(86400), today.plusDays(14), today));
        save(new Quote("COT-1002", new QuoteInput(vehicles.get(2), VehicleUse.COMMERCIAL, true,
                "5000", today.minusYears(42), 20, 1, PaymentMethod.OTHER, "28456789"),
                now.minusSeconds(172800), today.plusDays(13), today));
        save(new Quote("COT-1003", new QuoteInput(vehicles.get(3), VehicleUse.PRIVATE, false,
                "2000", today.minusYears(28), 7, 0, PaymentMethod.OTHER, "37890123"),
                now.minusSeconds(1728000), today.minusDays(5), today.minusDays(20)));
    }

    @Override
    public List<Vehicle> findAllVehicles() {
        return vehicles;
    }

    @Override
    public Optional<Quote> findById(String id) {
        return Optional.ofNullable(quotes.get(id));
    }

    @Override
    public Quote save(Quote quote) {
        quotes.put(quote.getId(), quote);
        return quote;
    }
}
