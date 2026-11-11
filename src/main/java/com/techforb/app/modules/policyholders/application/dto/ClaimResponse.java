package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.ClaimRecord;
import com.techforb.app.modules.policyholders.domain.ClaimStatus;
import java.time.LocalDate;

public record ClaimResponse(
        String number, ClaimStatus status, String vehicleId, String vehiclePlate,
        String reason, LocalDate reportedDate) {

    public static ClaimResponse from(ClaimRecord claim, String vehiclePlate) {
        return new ClaimResponse(claim.number(), claim.status(), claim.vehicleId(), vehiclePlate,
                claim.reason(), claim.reportedDate());
    }
}
