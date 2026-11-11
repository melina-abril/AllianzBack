package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.PolicyRecord;
import com.techforb.app.modules.policyholders.domain.PolicyStatus;
import java.time.LocalDate;
import java.util.List;

public record PolicyResponse(
        String number, PolicyStatus status, String coverageDescription, List<String> vehicleIds,
        LocalDate startDate, LocalDate endDate) {

    public static PolicyResponse from(PolicyRecord policy) {
        return new PolicyResponse(policy.number(), policy.status(), policy.coverageDescription(),
                policy.vehicleIds(), policy.startDate(), policy.endDate());
    }
}
