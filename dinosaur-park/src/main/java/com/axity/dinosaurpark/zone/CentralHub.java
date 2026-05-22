package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class CentralHub implements ParkZone {

    private final String name = "Central Hub";
    private final Set<Tourist> currentTourists = new HashSet<>();
    private final double souvenirPrice;
    private final double souvenirPurchaseProbability;
    private static final AtomicLong revenueIdGenerator = new AtomicLong(10000);

    public CentralHub() {
        ParkConfig config = ParkConfig.getInstance();
        this.souvenirPrice = config.getDouble("hub.souvenirPrice", 15.0);
        this.souvenirPurchaseProbability = config.getDouble("hub.souvenirPurchaseProbability", 0.4);
    }

    public void visit(Tourist tourist, Random rng, DatabaseService databaseService, double discount) {
        if (tourist == null) return;

        enter(tourist);
        tourist.recordVisit(this.name);

        // Compra por DealsHour
        if (rng.nextDouble() < souvenirPurchaseProbability) {
            double finalPrice = souvenirPrice * (1.0 - discount);
            tourist.spend(finalPrice);

            RevenueRecord record = new RevenueRecord(
                    revenueIdGenerator.getAndIncrement(),
                    "SOUVENIR_PURCHASE",
                    finalPrice,
                    tourist.getID(),
                    this.name,
                    LocalDateTime.now()
            );

            if (databaseService != null) {
                databaseService.saveRevenue(record);
            }
        }

        exit(tourist);
    }

    @Override
    public String getName() { return this.name; }

    @Override
    public boolean hasCapacity() { return true; }

    @Override
    public int getCurrentOccupancy() { return currentTourists.size(); }

    @Override
    public int getMaxCapacity() { return Integer.MAX_VALUE; }

    @Override
    public void enter(Tourist tourist) { if (tourist != null) currentTourists.add(tourist); }

    @Override
    public void exit(Tourist tourist) { if (tourist != null) currentTourists.remove(tourist); }
}
