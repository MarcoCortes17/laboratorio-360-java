package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class ObservationEnclosure implements ParkZone {

    private final String name;
    private final ExperienceType experienceType;
    private final Set<Tourist> currentVisitors = new HashSet<>();
    private final List<SatisfactionSurvey> surveys = new ArrayList<>();

    private final int maxVisitors;
    private final double entryFee;
    private static final AtomicLong revenueIdGenerator = new AtomicLong(40000);

    public ObservationEnclosure(String name, ExperienceType experienceType) {
        this.name = name;
        this.experienceType = experienceType;

        // mapeo
        ParkConfig config = ParkConfig.getInstance();
        switch (experienceType) {
            case PREMIUM:
                this.maxVisitors = config.getInt("enclosure.premium.maxVisitors", 12);
                this.entryFee = config.getDouble("enclosure.premium.entryFee", 30.0);
                break;
            case VIP:
                this.maxVisitors = config.getInt("enclosure.vip.maxVisitors", 5);
                this.entryFee = config.getDouble("enclosure.vip.entryFee", 75.0);
                break;
            case BASIC:
            default:
                this.maxVisitors = config.getInt("enclosure.basic.maxVisitors", 20);
                this.entryFee = config.getDouble("enclosure.basic.entryFee", 10.0);
                break;
        }
    }

    public boolean visit(Tourist tourist, Random rng, DatabaseService databaseService) {
        if (tourist == null || !hasCapacity()) {
            return false;
        }

        enter(tourist);
        tourist.spend(this.entryFee);
        tourist.recordVisit(this.name);

        RevenueRecord record = new RevenueRecord(
                revenueIdGenerator.getAndIncrement(),
                "ENCLOSURE_ENTRY_" + experienceType.name(),
                this.entryFee,
                tourist.getID(),
                this.name,
                LocalDateTime.now()
        );

        if (databaseService != null) {
            databaseService.saveRevenue(record);
        }

        // encuesta de satisfacción
        conductSurvey(tourist, rng);

        exit(tourist); 
        return true;
    }

    private void conductSurvey(Tourist tourist, Random rng) {
        int score;
        // Puntuaciones paramétricas (Mínimo..Máximo inclusive)
        switch (experienceType) {
            case PREMIUM:
                score = rng.nextInt(3) + 2; // Rango 2 - 4
                break;
            case VIP:
                score = rng.nextInt(3) + 3; // Rango 3 - 5
                break;
            case BASIC:
            default:
                score = rng.nextInt(3) + 1; // Rango 1 - 3
                break;
        }

        // cargar límites validados de SatisfactionSurvey
        SatisfactionSurvey survey = new SatisfactionSurvey(tourist.getID(), this.name, score);
        surveys.add(survey);
    }

    public List<SatisfactionSurvey> getSurveys() { return this.surveys; }

    public ExperienceType getExperienceType() { return this.experienceType; }

    @Override
    public String getName() { return this.name; }

    @Override
    public boolean hasCapacity() { return currentVisitors.size() < maxVisitors; }

    @Override
    public int getCurrentOccupancy() { return currentVisitors.size(); }

    @Override
    public int getMaxCapacity() { return this.maxVisitors; }

    @Override
    public void enter(Tourist tourist) { if (tourist != null) currentVisitors.add(tourist); }

    @Override
    public void exit(Tourist tourist) { if (tourist != null) currentVisitors.remove(tourist); }
}
