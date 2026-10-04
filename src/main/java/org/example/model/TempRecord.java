package org.example.model;

import java.time.LocalDateTime;

public class TempRecord {

    private int id;
    private double value;
    private int unitId;
    private double convertedValue;
    private int convertedUnitId;
    private LocalDateTime createdAt;

    public TempRecord(
            int id,
            double value,
            int unitId,
            double convertedValue,
            int convertedUnitId,
            LocalDateTime createdAt) {

        this.id = id;
        this.value = value;
        this.unitId = unitId;
        this.convertedValue = convertedValue;
        this.convertedUnitId = convertedUnitId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public int getUnitId() {
        return unitId;
    }

    public double getConvertedValue() {
        return convertedValue;
    }

    public int getConvertedUnitId() {
        return convertedUnitId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}