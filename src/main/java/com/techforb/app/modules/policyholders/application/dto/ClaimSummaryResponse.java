package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.ClaimRecord;
import com.techforb.app.modules.policyholders.domain.ClaimStatus;
import com.techforb.app.modules.policyholders.domain.Vehicle;
import java.time.LocalDate;
import java.util.List;

public record ClaimSummaryResponse(String number, ClaimStatus status, String vehiclePlate,
        String reason, LocalDate reportedDate) {

    public static ClaimSummaryResponse from(ClaimRecord claim, List<Vehicle> vehicles) {
        String plate = vehicles.stream().filter(vehicle -> vehicle.getId().equals(claim.vehicleId()))
                .map(Vehicle::getPlate).findFirst().orElse(null);
        return new ClaimSummaryResponse(claim.number(), claim.status(), plate,
                claim.reason(), claim.reportedDate());
    }
}
