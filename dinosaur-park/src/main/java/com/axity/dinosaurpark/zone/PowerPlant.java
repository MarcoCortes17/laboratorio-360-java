package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PowerPlant implements ParkZone {

    private final String name = "Power Plant";
    private boolean operational = true;
    private double currentEnergy;

    private final double initialEnergy;
    private final double consumptionPerStep;
    private final double failureProbability;
    private final double maintenanceCost;
    
    private static final AtomicLong expenseIdGenerator = new AtomicLong(30000);

    public PowerPlant() {
        ParkConfig config = ParkConfig.getInstance();
        this.initialEnergy = config.getDouble("powerplant.initialEnergy", 100.0);
        this.consumptionPerStep = config.getDouble("powerplant.consumptionPerStep", 1.5);
        this.failureProbability = config.getDouble("powerplant.failureProbability", 0.05);
        this.maintenanceCost = config.getDouble("powerplant.maintenanceCost", 200.0);
        this.currentEnergy = initialEnergy;
    }

    public void tick(Random rng, DatabaseService databaseService, long currentStep) {
        if (!operational) return;

        // consumo de energía por step
        currentEnergy -= consumptionPerStep;
        if (currentEnergy < 0) currentEnergy = 0;

        // egresos operativos en base de datos
        ExpenseRecord expense = new ExpenseRecord(
                expenseIdGenerator.getAndIncrement(),
                "POWER_PLANT_MAINTENANCE",
                maintenanceCost,
                "Consumo de combustible y costo operativo del generador por tick",
                LocalDateTime.now()
        );
        if (databaseService != null) {
            databaseService.saveExpense(expense);
        }

        // riesgo probabilístico de falla
        if (currentEnergy <= 0 || rng.nextDouble() < failureProbability) {
            triggerFailure(databaseService, currentStep);
        }
    }

    public void triggerFailure(DatabaseService databaseService, long currentStep) {
        this.operational = false;
        this.currentEnergy = 0.0;

        EventRecord event = new EventRecord(
                currentStep,
                "BLACKOUT",
                "Apagón total del complejo técnico por falla crítica en la planta.",
                "PowerPlant",
                LocalDateTime.now()
        );

        if (databaseService != null) {
            databaseService.saveEvent(event);
        }
    }

    public void repair() {
        this.operational = true;
        this.currentEnergy = this.initialEnergy;
    }

    public boolean isOperational() { return this.operational; }

    public double getEnergyLevel() { return (this.currentEnergy / this.initialEnergy) * 100.0; }

    @Override
    public String getName() { return this.name; }

    @Override
    public boolean hasCapacity() { return false; }

    @Override
    public int getCurrentOccupancy() { return 0; }

    @Override
    public int getMaxCapacity() { return 0; }

    @Override
    public void enter(Tourist tourist) {}

    @Override
    public void exit(Tourist tourist) {}
}
