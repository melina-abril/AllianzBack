package com.techforb.app.modules.policyholders.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Policyholder {

    private final String id;
    private PersonType personType;
    private String name;
    private String documentNumber;
    private PolicyholderStatus status;
    private String primaryContact;
    private String email;
    private String phone;
    private String address;
    private final List<PolicyRecord> policies;
    private final List<ClaimRecord> claims;
    private Instant updatedAt;

    public Policyholder(String id, PersonType personType, String name, String documentNumber,
            PolicyholderStatus status, String primaryContact, String email, String phone, String address,
            List<PolicyRecord> policies, List<ClaimRecord> claims, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.status = Objects.requireNonNull(status);
        this.policies = List.copyOf(policies);
        this.claims = List.copyOf(claims);
        updateDetails(personType, name, documentNumber, primaryContact, email, phone, address, updatedAt);
    }

    public void updateDetails(PersonType personType, String name, String documentNumber,
            String primaryContact, String email, String phone, String address, Instant updatedAt) {
        this.personType = Objects.requireNonNull(personType);
        this.name = name.trim();
        this.documentNumber = documentNumber;
        this.primaryContact = primaryContact == null ? "" : primaryContact.trim();
        this.email = email == null ? "" : email.trim();
        this.phone = phone == null ? "" : phone.trim();
        this.address = address == null ? "" : address.trim();
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public void deactivate(Instant at) {
        this.status = PolicyholderStatus.INACTIVE;
        this.updatedAt = at;
    }

    public int activePolicyCount(LocalDate today) {
        return (int) policies.stream().filter(policy -> policy.isActiveOn(today)).count();
    }

    public boolean isCovered(String vehicleId, LocalDate today) {
        return policies.stream().anyMatch(policy -> policy.isActiveOn(today) && policy.vehicleIds().contains(vehicleId));
    }

    public String getId() { return id; }
    public PersonType getPersonType() { return personType; }
    public String getName() { return name; }
    public String getDocumentNumber() { return documentNumber; }
    public PolicyholderStatus getStatus() { return status; }
    public String getPrimaryContact() { return primaryContact; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public List<PolicyRecord> getPolicies() { return policies; }
    public List<ClaimRecord> getClaims() { return claims; }
    public Instant getUpdatedAt() { return updatedAt; }
}
