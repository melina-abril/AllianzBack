package com.techforb.app.modules.policyholders.domain;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {

    List<Vehicle> findByPolicyholderId(String policyholderId);

    Optional<Vehicle> findById(String id);

    Optional<Vehicle> findByPlate(String plate);

    Vehicle save(Vehicle vehicle);
}
