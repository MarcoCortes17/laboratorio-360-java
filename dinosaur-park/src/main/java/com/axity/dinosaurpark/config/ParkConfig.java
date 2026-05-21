package com.axity.dinosaurpark.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ParkConfig {

    private static ParkConfig instance;
    private final Properties props;

    // Constructor PRIVADO — nadie puede hacer "new ParkConfig()"
    private ParkConfig() {
        props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("park.properties")) {
            if (input == null) {
                throw new RuntimeException("Error: No se encontró el archivo park.properties en resources.");
            }
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error crítico al leer las propiedades del parque.", ex);
        }
    }

    // Punto de acceso global — crea la instancia solo si no existe
    public static ParkConfig getInstance() {
        if (instance == null) {
            instance = new ParkConfig();
        }
        return instance;
    }

    // Métodos de lectura
    public int    getInt   (String key, int defaultValue)    {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public double getDouble(String key, double defaultValue) {
    String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public String getString(String key, String defaultValue) {
        String value = props.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    public long   getSeed  () {
        String value = props.getProperty("simulation.seed");
        if (value == null || value.trim().isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }  // lee simulation.seed
    public int    getTotalSteps() {
        return getInt("simulation.totalSteps",100);
    }

    // Solo para tests — permite resetear la instancia entre tests
    static void resetForTesting() { instance = null; }
}
