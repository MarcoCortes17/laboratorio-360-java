package com.axity.dinosaurpark.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvWriter {

    private final String revenuePath;
    private final String expensePath;
    private final String eventPath;

    public CsvWriter(String outputDir) {
        // asegurar que la carpeta contenedora exista 
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // definir las rutas completas de los archivos
        this.revenuePath = outputDir + File.separator + "revenues.csv";
        this.expensePath = outputDir + File.separator + "expenses.csv";
        this.eventPath = outputDir + File.separator + "events.csv";

        // inicializar los archivos SOBREESCRIBIENDO (false) e inyectando las cabeceras
        initFile(revenuePath, "id,type,amount,touristId,zone,timestamp");
        initFile(expensePath, "id,type,amount,description,timestamp");
        initFile(eventPath, "step,eventName,description,affectedEntities,timestamp");
    }

    private void initFile(String path, String header) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, false))) {
            pw.println(header);
        } catch (IOException e) {
            throw new RuntimeException("Error crítico al inicializar el archivo CSV: " + path, e);
        }
    }

    // métodos Append Mode

    public void appendRevenue(RevenueRecord r) {
        appendToFile(revenuePath, r.toCsvLine());
    }

    public void appendExpense(ExpenseRecord e) {
        appendToFile(expensePath, e.toCsvLine());
    }

    public void appendEvent(EventRecord ev) {
        appendToFile(eventPath, ev.toCsvLine());
    }

    private void appendToFile(String path, String line) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, true))) {
            pw.println(line);
        } catch (IOException e) {
            System.err.println("No se pudo escribir en el histórico de persistencia: " + path + " -> " + e.getMessage());
        }
    }
}
