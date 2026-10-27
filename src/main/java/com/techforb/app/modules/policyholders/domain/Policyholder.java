package com.techforb.app.modules.policyholders.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Entidad en memoria. Las asociaciones de pólizas y siniestros son datos de consulta de ejemplo. */
public class Policyholder {

    public enum PersonType { INDIVIDUAL, LEGAL_ENTITY }
    public enum Status { ACTIVE, INACTIVE }

    public record Vehicle(String id, String plate, String brand, String model, int year,
            String engineNumber, String chassisNumber) {
    }

    public record PolicySummary(String number, String status, String coverageDescription,
            List<String> vehicleIds, LocalDate startDate, LocalDate endDate) {
        public PolicySummary {
            vehicleIds = List.copyOf(vehicleIds);
        }
    }

    public record ClaimSummary(String number, String status, String vehicleId, String vehiclePlate,
            String reason, LocalDate reportedDate) {
    }

    private final String id;
    private PersonType personType;
    private String name;
    private String documentNumber;
    private Status status;
    private String primaryContact;
    private String email;
    private String phone;
    private String address;
    private Instant updatedAt;
    private final List<Vehicle> vehicles;
    private final List<PolicySummary> policies;
    private final List<ClaimSummary> claims;

    public Policyholder(String id, PersonType personType, String name, String documentNumber,
            String primaryContact, String email, String phone, String address, Status status, Instant updatedAt,
            List<Vehicle> vehicles, List<PolicySummary> policies, List<ClaimSummary> claims) {
        this.id = Objects.requireNonNull(id);
        this.status = Objects.requireNonNull(status);
        this.vehicles = new ArrayList<>(vehicles);
        this.policies = new ArrayList<>(policies);
        this.claims = new ArrayList<>(claims);
        updateDetails(personType, name, documentNumber, primaryContact, email, phone, address, updatedAt);
    }

    public void updateDetails(PersonType personType, String name, String documentNumber,
            String primaryContact, String email, String phone, String address, Instant updatedAt) {
        this.personType = Objects.requireNonNull(personType);
        this.name = Objects.requireNonNull(name);
        this.documentNumber = Objects.requireNonNull(documentNumber);
        this.primaryContact = primaryContact;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public void addVehicle(Vehicle vehicle, Instant updatedAt) {
        vehicles.add(vehicle);
        this.updatedAt = updatedAt;
    }

    public void replaceVehicle(Vehicle vehicle, Instant updatedAt) {
        for (int index = 0; index < vehicles.size(); index++) {
            if (vehicles.get(index).id().equals(vehicle.id())) {
                vehicles.set(index, vehicle);
                this.updatedAt = updatedAt;
                return;
            }
        }
        throw new IllegalArgumentException("El vehículo no pertenece al asegurado.");
    }

    public void deactivate(Instant updatedAt) {
        status = Status.INACTIVE;
        this.updatedAt = updatedAt;
    }

    public int activePolicyCount() {
        return (int) policies.stream().filter(policy -> "ACTIVE".equals(policy.status())).count();
    }

    public boolean isCovered(String vehicleId) {
        return policies.stream().anyMatch(policy -> "ACTIVE".equals(policy.status())
                && policy.vehicleIds().contains(vehicleId));
    }

    public String getId() { return id; }
    public PersonType getPersonType() { return personType; }
    public String getName() { return name; }
    public String getDocumentNumber() { return documentNumber; }
    public Status getStatus() { return status; }
    public String getPrimaryContact() { return primaryContact; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<Vehicle> getVehicles() { return List.copyOf(vehicles); }
    public List<PolicySummary> getPolicies() { return List.copyOf(policies); }
    public List<ClaimSummary> getClaims() { return List.copyOf(claims); }
}
