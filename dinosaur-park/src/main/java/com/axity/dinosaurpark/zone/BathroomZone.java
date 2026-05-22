package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class BathroomZone implements ParkZone {

    private final String name = "Bathroom & Spa";
    private final Map<Tourist, Integer> slots = new HashMap<>(); // mapa de tiempo
    
    private final int maxCapacity;
    private final int useDurationSteps;
    private final double spaPrice;
    private final double spaPurchaseProbability;
    private static final AtomicLong revenueIdGenerator = new AtomicLong(20000);

    public BathroomZone() {
        ParkConfig config = ParkConfig.getInstance();
        this.maxCapacity = config.getInt("bathroom.maxCapacity", 10);
        this.useDurationSteps = config.getInt("bathroom.useDurationSteps", 3);
        this.spaPrice = config.getDouble("bathroom.spaPrice", 20.0);
        this.spaPurchaseProbability = config.getDouble("bathroom.spaPurchaseProbability", 0.2);
    }

    public boolean tryEnter(Tourist tourist, Random rng, DatabaseService databaseService) {
        if (tourist == null || !hasCapacity()) {
            return false;
        }

        tourist.recordVisit(this.name);
        slots.put(tourist, useDurationSteps); // se asigna tiempo de retención

        // Compra opcional y probabilística de SPA
        if (rng.nextDouble() < spaPurchaseProbability) {
            tourist.spend(spaPrice);

            RevenueRecord record = new RevenueRecord(
                    revenueIdGenerator.getAndIncrement(),
                    "SPA_SERVICE",
                    spaPrice,
                    tourist.getID(),
                    this.name,
                    LocalDateTime.now()
            );

            if (databaseService != null) {
                databaseService.saveRevenue(record);
            }
        }
        return true;
    }

    public void tick() {
        Iterator<Map.Entry<Tourist, Integer>> iterator = slots.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Tourist, Integer> entry = iterator.next();
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                iterator.remove(); // turista termina su uso y desocupa el slot
            } else {
                entry.setValue(remaining);
            }
        }
    }

    @Override
    public String getName() { return this.name; }

    @Override
    public boolean hasCapacity() { return slots.size() < maxCapacity; }

    @Override
    public int getCurrentOccupancy() { return slots.size(); }

    @Override
    public int getMaxCapacity() { return this.maxCapacity; }

    @Override
    public void enter(Tourist tourist) {}

    @Override
    public void exit(Tourist tourist) { if (tourist != null) slots.remove(tourist); }
}
