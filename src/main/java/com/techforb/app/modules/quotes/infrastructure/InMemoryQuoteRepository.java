package com.techforb.app.modules.quotes.infrastructure;

import com.techforb.app.modules.quotes.domain.PaymentMethod;
import com.techforb.app.modules.quotes.domain.Quote;
import com.techforb.app.modules.quotes.domain.QuoteDetails;
import com.techforb.app.modules.quotes.domain.QuoteRepository;
import com.techforb.app.modules.quotes.domain.VehicleOption;
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

/** Datos DUMMY en memoria: reemplazar por un repositorio y valuaciones reales. */
@Repository
public class InMemoryQuoteRepository implements QuoteRepository {

    private final Map<String, Quote> quotes = new ConcurrentHashMap<>();
    private final List<VehicleOption> vehicles = List.of(
            new VehicleOption("Toyota", "Corolla", "XEI 2.0 CVT", 2023, new BigDecimal("24500000.00")),
            new VehicleOption("Toyota", "Corolla", "SEG 2.0 CVT", 2024, new BigDecimal("31000000.00")),
            new VehicleOption("Volkswagen", "Polo", "Track 1.6", 2022, new BigDecimal("17400000.00")),
            new VehicleOption("Volkswagen", "Polo", "Highline 1.0 TSI", 2024, new BigDecimal("26700000.00")),
            new VehicleOption("Ford", "Ranger", "XLT 2.0", 2023, new BigDecimal("38900000.00")));

    public InMemoryQuoteRepository(Clock clock) {
        LocalDate today = LocalDate.now(clock);
        Instant now = Instant.now(clock);
        seed("COT-1001", new QuoteDetails("Toyota", "Corolla", "XEI 2.0 CVT", 2023,
                VehicleUse.PERSONAL, false, "1425", LocalDate.of(1987, 6, 18), 14, 0,
                PaymentMethod.AUTO_DEBIT, "28456789"), vehicles.get(0), now.minusSeconds(172800), today.plusDays(13));
        seed("COT-1002", new QuoteDetails("Volkswagen", "Polo", "Track 1.6", 2022,
                VehicleUse.PERSONAL, true, "5000", LocalDate.of(1995, 3, 9), 6, 1,
                PaymentMethod.OTHER, "35781234"), vehicles.get(2), now.minusSeconds(864000), today.plusDays(5));
        seed("COT-1003", new QuoteDetails("Ford", "Ranger", "XLT 2.0", 2023,
                VehicleUse.COMMERCIAL, false, "2000", LocalDate.of(1979, 11, 2), 20, 0,
                PaymentMethod.OTHER, "23123456"), vehicles.get(4), now.minusSeconds(1728000), today.minusDays(5));
    }

    private void seed(String number, QuoteDetails details, VehicleOption vehicle, Instant createdAt, LocalDate expiresOn) {
        save(Quote.create(number, details, vehicle, createdAt, expiresOn));
    }

    @Override
    public List<VehicleOption> findAllVehicles() {
        return vehicles;
    }

    @Override
    public Optional<VehicleOption> findVehicle(String brand, String model, String version, int year) {
        return vehicles.stream().filter(option -> option.brand().equals(brand)
                && option.model().equals(model) && option.version().equals(version)
                && option.year() == year).findFirst();
    }

    @Override
    public Optional<Quote> findByNumber(String number) {
        return Optional.ofNullable(quotes.get(number));
    }

    @Override
    public Quote save(Quote quote) {
        quotes.put(quote.getNumber(), quote);
        return quote;
    }
}
