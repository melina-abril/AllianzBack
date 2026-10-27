package com.techforb.app.modules.policyholders.domain;

import java.util.Objects;

public class Vehicle {

    private final String id;
    private String plate;
    private String brand;
    private String model;
    private int year;
    private String engine;
    private String chassis;

    public Vehicle(String id, String plate, String brand, String model, int year, String engine, String chassis) {
        this.id = Objects.requireNonNull(id);
        update(plate, brand, model, year, engine, chassis);
    }

    public void update(String plate, String brand, String model, int year, String engine, String chassis) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.engine = engine;
        this.chassis = chassis;
    }

    public String getId() { return id; }
    public String getPlate() { return plate; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getEngine() { return engine; }
    public String getChassis() { return chassis; }
}
