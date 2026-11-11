package com.techforb.app.modules.quotes.domain;

import java.util.List;
import java.util.Optional;

public interface VehicleCatalogRepository {

    List<VehicleOption> findAll();

    Optional<VehicleOption> find(String brand, String model, String version, int year);
}
