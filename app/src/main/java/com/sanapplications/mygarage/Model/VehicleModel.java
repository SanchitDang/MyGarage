package com.sanapplications.mygarage.Model;

public class VehicleModel {
    private long id;
    private String make;
    private String model;

    public VehicleModel() {}

    public VehicleModel(String make, String model) {
        this.make = make;
        this.model = model;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return make + " " + model;
    }
}
