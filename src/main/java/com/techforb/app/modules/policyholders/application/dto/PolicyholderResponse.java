package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.PersonType;
import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.PolicyholderStatus;
import com.techforb.app.modules.policyholders.domain.Vehicle;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record PolicyholderResponse(
        String id, PersonType personType, String name, String documentNumber, PolicyholderStatus status,
        String primaryContact, String email, String phone, String address, int vehicleCount,
        int activePolicyCount, List<VehicleResponse> vehicles, List<PolicyResponse> policies,
        List<ClaimResponse> claims, Instant updatedAt) {

    public static PolicyholderResponse from(Policyholder holder, List<Vehicle> vehicles, LocalDate today) {
        List<VehicleResponse> vehicleResponses = vehicles.stream()
                .map(vehicle -> VehicleResponse.from(vehicle, holder.isCovered(vehicle.getId(), today)))
                .toList();
        List<PolicyResponse> policyResponses = holder.getPolicies().stream().map(PolicyResponse::from).toList();
        List<ClaimResponse> claimResponses = holder.getClaims().stream()
                .map(claim -> ClaimResponse.from(claim, vehicles.stream()
                        .filter(vehicle -> vehicle.getId().equals(claim.vehicleId()))
                        .map(Vehicle::getPlate).findFirst().orElse("No informado")))
                .toList();
        return new PolicyholderResponse(holder.getId(), holder.getPersonType(), holder.getName(),
                holder.getDocumentNumber(), holder.getStatus(), holder.getPrimaryContact(), holder.getEmail(),
                holder.getPhone(), holder.getAddress(), vehicles.size(), holder.activePolicyCount(today),
                vehicleResponses, policyResponses, claimResponses, holder.getUpdatedAt());
    }
}
