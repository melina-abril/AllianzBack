package com.techforb.app.modules.quotes.domain;

import java.util.List;
import java.util.Optional;

/** Puerto para cotizaciones y catálogo disponibles en este módulo. */
public interface QuoteRepository {

    List<Vehicle> findAllVehicles();

    Optional<Quote> findById(String id);

    Quote save(Quote quote);
}
