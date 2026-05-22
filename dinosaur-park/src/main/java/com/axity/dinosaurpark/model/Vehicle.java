package com.axity.dinosaurpark.model;

public class Vehicle {

    private final int id;
    private final String plate;
    private final int repairSteps;
    private VehicleStatus status;
    private int repairCountdown;

    public Vehicle(int id, String plate, int repairSteps) {
        this.id = id;
        this.plate = plate;
        this.repairSteps = repairSteps;
        this.status = VehicleStatus.AVAILABLE;
        this.repairCountdown = 0;
    }

    public int getId() { return id; }
    public String getPlate() { return plate; }
    public VehicleStatus getStatus() { return status; }
    public int getRepairCountdown() { return repairCountdown; }
    public int getRepairSteps() { return repairSteps; }

    public void use() {
        if (status == VehicleStatus.AVAILABLE) {
            status = VehicleStatus.IN_USE;
        }
    }

    public void free() {
        if (status == VehicleStatus.IN_USE) {
            status = VehicleStatus.AVAILABLE;
        }
    }

    public void markBroken() {
        this.status = VehicleStatus.BROKEN;
        this.repairCountdown = repairSteps;
    }

    public void tick() {
        if (status == VehicleStatus.BROKEN) {
            repairCountdown--;
            if (repairCountdown <= 0) {
                status = VehicleStatus.AVAILABLE;
                repairCountdown = 0;
            }
        }
    }
}
