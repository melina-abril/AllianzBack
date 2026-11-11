package com.techforb.app.modules.quotes.infrastructure;

import com.techforb.app.modules.quotes.domain.PaymentMethod;
import com.techforb.app.modules.quotes.domain.Quote;
import com.techforb.app.modules.quotes.domain.QuoteData;
import com.techforb.app.modules.quotes.domain.QuoteRepository;
import com.techforb.app.modules.quotes.domain.VehicleCatalogRepository;
import com.techforb.app.modules.quotes.domain.VehicleOption;
import com.techforb.app.modules.quotes.domain.VehicleUse;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** Cotizaciones DUMMY en memoria: reemplazar por persistencia real sin cambiar el service. */
@Repository
public class InMemoryQuoteRepository implements QuoteRepository {

    private final Map<String, Quote> quotes = new ConcurrentHashMap<>();

    public InMemoryQuoteRepository(VehicleCatalogRepository catalog, Clock clock) {
        VehicleOption corolla = catalog.find("Toyota", "Corolla", "XEI 2.0 CVT", 2023).orElseThrow();
        VehicleOption polo = catalog.find("Volkswagen", "Polo", "Track 1.6", 2023).orElseThrow();
        VehicleOption cronos = catalog.find("Fiat", "Cronos", "Drive 1.3", 2023).orElseThrow();
        LocalDate today = LocalDate.now(clock);
        Instant now = Instant.now(clock);
        save(new Quote("quo-1001", new QuoteData("30123456", "Toyota", "Corolla", "XEI 2.0 CVT", 2023,
                VehicleUse.PERSONAL, false, "1425", LocalDate.of(1987, 5, 12), 17, 0,
                PaymentMethod.AUTOMATIC_DEBIT), corolla, now.minusSeconds(2 * 86400L), today.minusDays(2)));
        save(new Quote("quo-1002", new QuoteData("27567890", "Volkswagen", "Polo", "Track 1.6", 2023,
                VehicleUse.COMMERCIAL, true, "5000", LocalDate.of(1992, 8, 21), 10, 1,
                PaymentMethod.OTHER), polo, now.minusSeconds(5 * 86400L), today.minusDays(5)));
        save(new Quote("quo-1003", new QuoteData("33444555", "Fiat", "Cronos", "Drive 1.3", 2023,
                VehicleUse.PERSONAL, false, "2000", LocalDate.of(1995, 3, 7), 8, 0,
                PaymentMethod.OTHER), cronos, now.minusSeconds(20 * 86400L), today.minusDays(20)));
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
