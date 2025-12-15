package entities;

import javax.persistence.*;

@Entity
@Table(name = "MaintenanceType")
public class MaintenanceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "maxMileage", nullable = false, length = 100)
    private int maxMileage;

    @Column(name = "maxDuration", nullable = false, length = 100)
    private int maxDuration;

    public MaintenanceType() {
    }

    public MaintenanceType(int maxMileage, int maxDuration) {
        this.maxMileage = maxMileage;
        this.maxDuration = maxDuration;
    }

    // getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                "id=" + id +
                ", maxMileage=" + maxMileage +
                ", maxDuration=" + maxDuration +
                '}';
    }
}