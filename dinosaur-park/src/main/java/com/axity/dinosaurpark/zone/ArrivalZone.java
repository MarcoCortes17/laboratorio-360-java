package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

public class ArrivalZone implements ParkZone {

    private final String name = "Arrival Zone";
    private final Queue<Tourist> waitingQueue = new LinkedList<>();
    private final double baseTicketPrice;
    private static final AtomicLong revenueIdGenerator = new AtomicLong(1);

    public ArrivalZone() {
        ParkConfig config = ParkConfig.getInstance();
        this.baseTicketPrice = config.getDouble("arrival.ticketPrice", 25.0);
    }

    public void addTouristToQueue(Tourist tourist) {
        if (tourist != null) {
            waitingQueue.add(tourist);
        }
    }

    public List<Tourist> processBatch(int batchSize, DatabaseService databaseService, double discount) {
        List<Tourist> processedTourists = new ArrayList<>();
        int processed = 0;
        double finalPrice = baseTicketPrice * (1.0 - discount); // descuento DealsHour

        while (!waitingQueue.isEmpty() && processed < batchSize) {
            Tourist tourist = waitingQueue.poll();
            if (tourist != null) {
                tourist.setStatus(TouristStatus.IN_PARK);
                tourist.spend(finalPrice);
                tourist.recordVisit(this.name);

                RevenueRecord record = new RevenueRecord(
                        revenueIdGenerator.getAndIncrement(),
                        "TICKET_SALE",
                        finalPrice,
                        tourist.getID(),
                        this.name,
                        LocalDateTime.now()
                );
                
                if (databaseService != null) {
                    databaseService.saveRevenue(record); // Inyección Base de Datos
                }

                processedTourists.add(tourist);
                processed++;
            }
        }
        return processedTourists;
    }

    @Override
    public String getName() { return this.name; }

    @Override
    public boolean hasCapacity() { return true; }

    @Override
    public int getCurrentOccupancy() { return waitingQueue.size(); }

    @Override
    public int getMaxCapacity() { return Integer.MAX_VALUE; }

    @Override
    public void enter(Tourist tourist) { addTouristToQueue(tourist); }

    @Override
    public void exit(Tourist tourist) { waitingQueue.remove(tourist); }
}
