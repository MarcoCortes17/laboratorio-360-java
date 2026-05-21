package com.axity.dinosaurpark.model;

import java.util.ArrayList;
import java.util.List;

public class Tourist {
    
    private final int id;               //identificador unico
    private final String name;          //nombre del turista
    private TouristStatus status;       //estado actual
    private double moneySpent;          //acumula lo que gasta
    private List<String> visitedZones;  //historial de zonas visitadas
    
    public Tourist(int id, String name) {
        this.id = id;
        this.name = name;
        this.status = TouristStatus.WAITING;
        this.moneySpent = 0.0;
        this.visitedZones = new ArrayList<>();
    }

    public void spend(double amount) {
        this.moneySpent += amount;
    }

    public void recordVisit(String zoneName) {
        this.visitedZones.add(zoneName);
    }
    

    // GETTER SETTER

    public int getID() { return id; }
    public String getName() { return name; }

    public TouristStatus getStatus() { return status; }
    public void setStatus(TouristStatus status) { this.status = status; }

    public double getMoneySpent() { return moneySpent; }
    public List<String> getVisitedZones() { return visitedZones; }
    
}
