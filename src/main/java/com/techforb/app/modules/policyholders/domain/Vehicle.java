package com.techforb.app.modules.policyholders.domain;

import java.time.Instant;
import java.util.Objects;

public class Vehicle {

    private final String id;
    private final String policyholderId;
    private String plate;
    private String make;
    private String model;
    private int year;
    private String engineNumber;
    private String chassisNumber;
    private Instant updatedAt;

    public Vehicle(String id, String policyholderId, String plate, String make, String model, int year,
            String engineNumber, String chassisNumber, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.policyholderId = Objects.requireNonNull(policyholderId);
        updateDetails(plate, make, model, year, engineNumber, chassisNumber, updatedAt);
    }

    public void updateDetails(String plate, String make, String model, int year,
            String engineNumber, String chassisNumber, Instant updatedAt) {
        this.plate = plate;
        this.make = make.trim();
        this.model = model.trim();
        this.year = year;
        this.engineNumber = engineNumber.trim();
        this.chassisNumber = chassisNumber.trim();
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public String getId() { return id; }
    public String getPolicyholderId() { return policyholderId; }
    public String getPlate() { return plate; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getEngineNumber() { return engineNumber; }
    public String getChassisNumber() { return chassisNumber; }
    public Instant getUpdatedAt() { return updatedAt; }
}
