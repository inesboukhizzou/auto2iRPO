package entities;

import jakarta.persistence.*;

/**
 * Represents a type of maintenance intervention.
 * Maintenance types have maximum mileage and duration thresholds
 * that determine when the next maintenance should occur.
 */
@Entity
@Table(name = "MaintenanceType")
public class MaintenanceType extends InterventionType {

    @Column(name = "maxMileage", nullable = false)
    private int maxMileage;

    @Column(name = "maxDuration", nullable = false)
    private int maxDuration;

    public MaintenanceType() {
        super();
    }

    public MaintenanceType(String name, int maxMileage, int maxDuration) {
        super(name);
        this.maxMileage = maxMileage;
        this.maxDuration = maxDuration;
    }

    

    public int getMaxMileage() {
        return maxMileage;
    }

    public void setMaxMileage(int maxMileage) {
        this.maxMileage = maxMileage;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    @Override
    public String toString() {
        return "MaintenanceType{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", maxMileage=" + maxMileage +
                ", maxDuration=" + maxDuration +
                '}';
    }
}