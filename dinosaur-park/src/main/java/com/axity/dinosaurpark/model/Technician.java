package com.axity.dinosaurpark.model;

import java.util.List;
import java.util.Optional;

public class Technician extends Worker {

    public Technician(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "TECHNICIAN";
    }

    //repara planta eléctrica no operacional
    //solo si hay vehículo disponible.
    public void repairIfNeeded(PowerPlant powerPlant, List<Vehicle> vehicles) {
        if (powerPlant == null || vehicles == null) return;

        if (!powerPlant.isOperational()) {
            // Busca el primer vehículo con status AVAILABLE
            Optional<Vehicle> availableVehicle = vehicles.stream()
                .filter(v -> v.getStatus() == VehicleStatus.AVAILABLE)
                .findFirst();

            if (availableVehicle.isPresent()) {
                Vehicle vehicle = availableVehicle.get();
                
                vehicle.use();     // Marca como IN_USE
                powerPlant.repair();    // Levanta la planta eléctrica
                vehicle.free();    // Devuelve el vehículo a AVAILABLE
            }
            // Si no hay vehículo: la planta queda sin reparar este step
        }
    }
    
}
