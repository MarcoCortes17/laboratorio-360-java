package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

public record EventRecord(
    long step, 
    String eventName, 
    String description,
    String affectedEntities, 
    LocalDateTime timestamp
) {
    public String toCsvLine() {
        return String.format("%d,%s,%s,%s,%s", step, eventName, description, affectedEntities, timestamp);
    }
}
