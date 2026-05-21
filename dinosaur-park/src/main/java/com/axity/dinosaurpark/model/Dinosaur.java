package com.axity.dinosaurpark.model;

public abstract class Dinosaur {
    // Campos comunes a todos los dinosaurios
    private final int id;
    private final String name, species;
    private DinosaurStatus status;  // inicia en IN_ENCLOSURE
    private final double feedingCostPerDay;

    public Dinosaur(int id, String name, String species, double feedingCostPerDay) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.status = DinosaurStatus.IN_ENCLOSURE;
        this.feedingCostPerDay = feedingCostPerDay;
    }

    // Métodos abstractos — cada subclase define su propio comportamiento
    public abstract String getDiet();        // "CARNIVORE" o "HERBIVORE"
    public abstract double getDangerLevel(); // 0.0 a 1.0

    // Métodos concretos — iguales para todos
    public void escape()           { this.status = DinosaurStatus.ESCAPED;     }
    public void recapture()        { this.status = DinosaurStatus.RECAPTURED;  }
    public void returnToEnclosure(){ this.status = DinosaurStatus.IN_ENCLOSURE;}

    //GETTER SETTER
    public int getID() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public DinosaurStatus getStatus() { return status; }
    public double getFeedingCostPerDay() { return feedingCostPerDay; }
    
}
