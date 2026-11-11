package com.techforb.app.modules.policyholders.infrastructure;

import com.techforb.app.modules.policyholders.domain.Vehicle;
import com.techforb.app.modules.policyholders.domain.VehicleRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** Vehículos DUMMY en memoria: reemplazar por un repositorio real al conectar el backend. */
@Repository
public class InMemoryVehicleRepository implements VehicleRepository {

    private final Map<String, Vehicle> vehicles = new ConcurrentHashMap<>();

    public InMemoryVehicleRepository() {
        save(new Vehicle("veh-1001", "ph-1001", "AF123BC", "Toyota", "Corolla", 2023,
                "2ZRA123456", "9BRBD3HEXP1234567", Instant.parse("2026-03-01T11:00:00Z")));
        save(new Vehicle("veh-1002", "ph-1001", "AC456DE", "Ford", "Ka", 2018,
                "SIGMA987654", "9BFZH55L8J1234567", Instant.parse("2026-02-20T10:00:00Z")));
        save(new Vehicle("veh-1003", "ph-1003", "AB789FG", "Volkswagen", "Gol Trend", 2017,
                "CFZA456789", "9BWAB45U7HT123456", Instant.parse("2024-01-02T09:00:00Z")));
        save(new Vehicle("veh-1004", "ph-1004", "AG321HI", "Peugeot", "208", 2024,
                "EB21234567", "8ADCC9HCHRG123456", Instant.parse("2025-04-10T15:00:00Z")));
    }

    @Override
    public List<Vehicle> findByPolicyholderId(String policyholderId) {
        return vehicles.values().stream()
                .filter(vehicle -> vehicle.getPolicyholderId().equals(policyholderId))
                .sorted(Comparator.comparing(Vehicle::getPlate)).toList();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return Optional.ofNullable(vehicles.get(id));
    }

    @Override
    public Optional<Vehicle> findByPlate(String plate) {
        return vehicles.values().stream().filter(vehicle -> vehicle.getPlate().equals(plate)).findFirst();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        vehicles.put(vehicle.getId(), vehicle);
        return vehicle;
    }
}
