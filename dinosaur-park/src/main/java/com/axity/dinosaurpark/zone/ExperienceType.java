package com.axity.dinosaurpark.zone;

import java.util.Random;

public enum ExperienceType {
    BASIC, PREMIUM, VIP;

    // puntuación por tipo (MIN..MAX, ambos inclusive):
 // public int generateSurveyScore(Random random) {
 //     return switch (this) {
 //         case BASIC -> random.nextInt(3) + 1;     // 1 - 3
 //         case PREMIUM -> random.nextInt(3) + 2;   // 2 - 4
 //         case VIP -> random.nextInt(3) + 3;       // 3 - 5
 //     };
 // }
}
