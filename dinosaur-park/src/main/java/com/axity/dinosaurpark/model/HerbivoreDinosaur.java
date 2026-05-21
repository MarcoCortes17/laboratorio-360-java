package com.axity.dinosaurpark.model;


public class HerbivoreDinosaur extends Dinosaur {

    private String diet = "HERBIVORE";
    private double dangerLevel = 0.2;

    //constructor
    public HerbivoreDinosaur(int id, String name, String species) {
        super(id, name, species, 200.0);
    }

    //Getters
    @Override
    public String getDiet() { return this.diet; }
    @Override
    public double getDangerLevel() { return this.dangerLevel; }
    
}
