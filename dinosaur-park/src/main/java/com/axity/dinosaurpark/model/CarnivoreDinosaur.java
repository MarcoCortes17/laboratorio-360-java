package com.axity.dinosaurpark.model;


public class CarnivoreDinosaur extends Dinosaur {

    private String diet = "CARNIVORE";
    private double dangerLevel = 0.9;

    //constructor
    public CarnivoreDinosaur(int id, String name, String species) {
        super(id, name, species, 500.0);
    }

    //Getters
    @Override
    public String getDiet() { return this.diet; }
    @Override
    public double getDangerLevel() { return this.dangerLevel; }
    
}
