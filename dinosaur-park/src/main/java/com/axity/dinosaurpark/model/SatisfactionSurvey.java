package com.axity.dinosaurpark.model;

public class SatisfactionSurvey {

    private final int touristId;
    private final String enclosureName;
    private final int score; // Rango 1 - 5

    public SatisfactionSurvey(int touristId, String enclosureName, int score) {
        this.touristId = touristId;
        this.enclosureName = enclosureName;
        
        // limites 1 - 5
        if (score < 1) {
            this.score = 1;
        } else if (score > 5) {
            this.score = 5;
        } else {
            this.score = score;
        }
    }

    // GETTERS
    public int getTouristId() { return touristId; }
    public String getEnclosureName() { return enclosureName; }
    public int getScore() { return score; }

}
