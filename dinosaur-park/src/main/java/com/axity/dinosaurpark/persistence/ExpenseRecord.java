package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

public record ExpenseRecord(
    long id, 
    String type, 
    double amount,
    String description, 
    LocalDateTime timestamp
) {
    public String toCsvLine() {
        return String.format("%d,%s,%.2f,%s,%s", id, type, amount, description, timestamp);
    }
}
