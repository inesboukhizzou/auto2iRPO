package entities;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="Vehicle")

public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Temporal(TemporalType.DATE)
    @Column(name = "dateOfFirstRegistration", nullable = false, length = 30)
    private Date dateOfFirstRegistration;

    @Column(name = "lastMileage", nullable = false, length = 100)
    private int lastMileage;

    public Vehicle(){
    }

    public Vehicle(Date dateOfFirstRegistration, int lastMileage){
        this.dateOfFirstRegistration = dateOfFirstRegistration;
        this.lastMileage=lastMileage;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOfFirstRegistration() {
        return dateOfFirstRegistration;
    }

    public void setDateOfFirstRegistration(Date dateOfFirstRegistration) {
        this.dateOfFirstRegistration = dateOfFirstRegistration;
    }

    public int getLastMileage() {
        return lastMileage;
    }

    public void setLastMileage(int lastMileage) {
        this.lastMileage = lastMileage;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", dateOfFirstRegistration=" + dateOfFirstRegistration +
                ", lastMileage=" + lastMileage +
                '}';
    }
}
