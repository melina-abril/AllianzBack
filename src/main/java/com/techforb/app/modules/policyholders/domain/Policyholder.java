package com.techforb.app.modules.policyholders.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Policyholder {

    private final String id;
    private PersonType personType;
    private String name;
    private DocumentType documentType;
    private String documentNumber;
    private String primaryContact;
    private String email;
    private String phone;
    private String address;
    private PolicyholderStatus status;
    private Instant updatedAt;
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<PolicyRecord> policies = new ArrayList<>();
    private final List<ClaimRecord> claims = new ArrayList<>();

    public Policyholder(String id, PersonType personType, String name, DocumentType documentType,
            String documentNumber, String primaryContact, String email, String phone,
            String address, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.status = PolicyholderStatus.ACTIVE;
        updateDetails(personType, name, documentType, documentNumber, primaryContact, email, phone, address, updatedAt);
    }

    public void updateDetails(PersonType personType, String name, DocumentType documentType,
            String documentNumber, String primaryContact, String email, String phone,
            String address, Instant updatedAt) {
        this.personType = personType;
        this.name = name;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.primaryContact = primaryContact;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.updatedAt = updatedAt;
    }

    public void addVehicle(Vehicle vehicle, Instant at) {
        vehicles.add(vehicle);
        touch(at);
    }

    public void addPolicy(PolicyRecord policy) { policies.add(policy); }
    public void addClaim(ClaimRecord claim) { claims.add(claim); }
    public void touch(Instant at) { updatedAt = at; }

    public void deactivate(Instant at) {
        status = PolicyholderStatus.INACTIVE;
        touch(at);
    }

    public boolean isActive() { return status == PolicyholderStatus.ACTIVE; }

    public int activePolicyCount(LocalDate today) {
        return (int) policies.stream().filter(policy -> policy.isActiveOn(today)).count();
    }

    public String getId() { return id; }
    public PersonType getPersonType() { return personType; }
    public String getName() { return name; }
    public DocumentType getDocumentType() { return documentType; }
    public String getDocumentNumber() { return documentNumber; }
    public String getPrimaryContact() { return primaryContact; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public PolicyholderStatus getStatus() { return status; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<Vehicle> getVehicles() { return List.copyOf(vehicles); }
    public List<PolicyRecord> getPolicies() { return List.copyOf(policies); }
    public List<ClaimRecord> getClaims() { return List.copyOf(claims); }
}
