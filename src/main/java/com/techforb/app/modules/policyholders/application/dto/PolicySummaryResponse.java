package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.PolicyRecord;
import com.techforb.app.modules.policyholders.domain.PolicyStatus;
import com.techforb.app.modules.policyholders.domain.Vehicle;
import java.time.LocalDate;
import java.util.List;

public record PolicySummaryResponse(String number, PolicyStatus status, String coverageDescription,
        List<String> vehiclePlates, LocalDate startDate, LocalDate endDate) {

    public static PolicySummaryResponse from(PolicyRecord policy, List<Vehicle> vehicles) {
        List<String> plates = vehicles.stream()
                .filter(vehicle -> policy.vehicleIds().contains(vehicle.getId()))
                .map(Vehicle::getPlate).toList();
        return new PolicySummaryResponse(policy.number(), policy.status(), policy.coverageDescription(),
                plates, policy.startDate(), policy.endDate());
    }
}
