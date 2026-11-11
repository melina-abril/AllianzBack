package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.PersonType;
import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.PolicyholderStatus;

public record PolicyholderSummaryResponse(
        String id, String name, PersonType personType, String documentNumber, PolicyholderStatus status) {

    public static PolicyholderSummaryResponse from(Policyholder holder) {
        return new PolicyholderSummaryResponse(holder.getId(), holder.getName(), holder.getPersonType(),
                holder.getDocumentNumber(), holder.getStatus());
    }
}
