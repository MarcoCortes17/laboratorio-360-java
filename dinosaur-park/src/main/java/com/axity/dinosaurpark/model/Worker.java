package com.axity.dinosaurpark.model;

public abstract class Worker {

    private final int id;
    private final String name;
    private final double dailySalary;

    public Worker(int id, String name, double dailySalary) {
        this.id = id;
        this.name = name;
        this.dailySalary = dailySalary;
    }

    // GETTERS
    public int getId() { return id; }
    public String getName() { return name; }
    public double getDailySalary() { return dailySalary; }

    public abstract String getRole(); // "Guard" o "Technician"

}
