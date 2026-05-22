package com.axity.dinosaurpark.model;

import java.util.List;

public class Guard extends Worker {

    public Guard(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "GUARD";
    }

    // lee lista de dinosaurios
    // si dinosaurio ESCAPED, lo regresa recinto
    public void recaptureEscapeDinosaurs(List<Dinosaur> dinosaurs) {
        if (dinosaurs == null) return;

        for (Dinosaur dinosaur : dinosaurs) {
            if (dinosaur.getStatus() == DinosaurStatus.ESCAPED) {
                dinosaur.returnToEnclosure();
            }
        }
    }
    
}
