package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

public record RevenueRecord(
    long id, 
    String type, 
    double amount,
    int touristId, 
    String zone, 
    LocalDateTime timestamp
) {
    public String toCsvLine() {
        return String.format("%d,%s,%.2f,%d,%s,%s", id, type, amount, touristId, zone, timestamp);
    }
}
