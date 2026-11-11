package com.techforb.app.modules.quotes.infrastructure;

import com.techforb.app.modules.quotes.domain.VehicleCatalogRepository;
import com.techforb.app.modules.quotes.domain.VehicleOption;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/** Catálogo y valuaciones DUMMY: reemplazar por información vigente del negocio. */
@Repository
public class InMemoryVehicleCatalogRepository implements VehicleCatalogRepository {

    private final List<VehicleOption> vehicles = List.of(
            new VehicleOption("Toyota", "Corolla", "XEI 2.0 CVT", 2023, new BigDecimal("24500000")),
            new VehicleOption("Toyota", "Corolla", "XEI 2.0 CVT", 2024, new BigDecimal("28500000")),
            new VehicleOption("Toyota", "Yaris", "XS 1.5 CVT", 2022, new BigDecimal("18500000")),
            new VehicleOption("Volkswagen", "Polo", "Track 1.6", 2023, new BigDecimal("20500000")),
            new VehicleOption("Volkswagen", "Polo", "Highline 1.0 TSI", 2024, new BigDecimal("26500000")),
            new VehicleOption("Fiat", "Cronos", "Drive 1.3", 2023, new BigDecimal("19500000")));

    @Override
    public List<VehicleOption> findAll() {
        return vehicles;
    }

    @Override
    public Optional<VehicleOption> find(String brand, String model, String version, int year) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.brand().equals(brand) && vehicle.model().equals(model)
                        && vehicle.version().equals(version) && vehicle.year() == year)
                .findFirst();
    }
}
