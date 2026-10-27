package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.DocumentType;
import com.techforb.app.modules.policyholders.domain.PersonType;
import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.PolicyholderStatus;
import java.time.LocalDate;
import java.util.List;

public record PolicyholderDetailResponse(String id, String name, PersonType personType,
        DocumentType documentType, String documentNumber, PolicyholderStatus status,
        String primaryContact, String email, String phone, String address,
        int vehicleCount, int activePolicyCount, List<VehicleResponse> vehicles,
        List<PolicySummaryResponse> policies, List<ClaimSummaryResponse> claims) {

    public static PolicyholderDetailResponse from(Policyholder policyholder, LocalDate today) {
        List<VehicleResponse> vehicles = policyholder.getVehicles().stream()
                .map(vehicle -> VehicleResponse.from(vehicle, policyholder.getPolicies(), today)).toList();
        List<PolicySummaryResponse> policies = policyholder.getPolicies().stream()
                .map(policy -> PolicySummaryResponse.from(policy, policyholder.getVehicles())).toList();
        List<ClaimSummaryResponse> claims = policyholder.getClaims().stream()
                .map(claim -> ClaimSummaryResponse.from(claim, policyholder.getVehicles())).toList();
        return new PolicyholderDetailResponse(policyholder.getId(), policyholder.getName(),
                policyholder.getPersonType(), policyholder.getDocumentType(), policyholder.getDocumentNumber(),
                policyholder.getStatus(), policyholder.getPrimaryContact(), policyholder.getEmail(),
                policyholder.getPhone(), policyholder.getAddress(), vehicles.size(),
                policyholder.activePolicyCount(today), vehicles, policies, claims);
    }
}
