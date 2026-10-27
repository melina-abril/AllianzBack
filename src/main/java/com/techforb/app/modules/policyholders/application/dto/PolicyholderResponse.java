package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.Policyholder.PersonType;
import com.techforb.app.modules.policyholders.domain.Policyholder.Status;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record PolicyholderResponse(String id, PersonType personType, String name, String documentNumber,
        Status status, String primaryContact, String email, String phone, String address,
        int vehicleCount, int activePolicyCount, List<VehicleResponse> vehicles,
        List<PolicyResponse> policies, List<ClaimResponse> claims, Instant updatedAt) {

    public static PolicyholderResponse from(Policyholder holder) {
        return new PolicyholderResponse(holder.getId(), holder.getPersonType(), holder.getName(),
                holder.getDocumentNumber(), holder.getStatus(), holder.getPrimaryContact(), holder.getEmail(),
                holder.getPhone(), holder.getAddress(), holder.getVehicles().size(), holder.activePolicyCount(),
                holder.getVehicles().stream().map(vehicle -> VehicleResponse.from(vehicle, holder.isCovered(vehicle.id()))).toList(),
                holder.getPolicies().stream().map(PolicyResponse::from).toList(),
                holder.getClaims().stream().map(ClaimResponse::from).toList(), holder.getUpdatedAt());
    }

    public record PolicyResponse(String number, String status, String coverageDescription,
            List<String> vehicleIds, LocalDate startDate, LocalDate endDate) {
        public static PolicyResponse from(Policyholder.PolicySummary policy) {
            return new PolicyResponse(policy.number(), policy.status(), policy.coverageDescription(),
                    policy.vehicleIds(), policy.startDate(), policy.endDate());
        }
    }

    public record ClaimResponse(String number, String status, String vehicleId, String vehiclePlate,
            String reason, LocalDate reportedDate) {
        public static ClaimResponse from(Policyholder.ClaimSummary claim) {
            return new ClaimResponse(claim.number(), claim.status(), claim.vehicleId(), claim.vehiclePlate(),
                    claim.reason(), claim.reportedDate());
        }
    }
}
