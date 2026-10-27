package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.DocumentType;
import com.techforb.app.modules.policyholders.domain.PersonType;
import com.techforb.app.modules.policyholders.domain.Policyholder;
import com.techforb.app.modules.policyholders.domain.PolicyholderStatus;

public record PolicyholderResponse(String id, String name, PersonType personType,
        DocumentType documentType, String documentNumber, PolicyholderStatus status) {

    public static PolicyholderResponse from(Policyholder policyholder) {
        return new PolicyholderResponse(policyholder.getId(), policyholder.getName(),
                policyholder.getPersonType(), policyholder.getDocumentType(),
                policyholder.getDocumentNumber(), policyholder.getStatus());
    }
}
