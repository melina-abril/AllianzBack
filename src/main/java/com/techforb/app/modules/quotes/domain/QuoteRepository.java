package com.techforb.app.modules.quotes.domain;

import java.util.List;
import java.util.Optional;

/** Puerto para las cotizaciones y las valuaciones disponibles. */
public interface QuoteRepository {

    List<VehicleOption> findAllVehicles();

    Optional<VehicleOption> findVehicle(String brand, String model, String version, int year);

    Optional<Quote> findByNumber(String number);

    Quote save(Quote quote);
}
