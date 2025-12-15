package entities;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="Vehicle")

public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation ManyToOne : plusieurs véhicules appartiennent à un propriétaire
    @ManyToOne
    @JoinColumn(name = "ownerId", nullable = false)
    private Owner owner;

    // Relation ManyToOne : plusieurs véhicules peuvent être du même type
    @ManyToOne
    @JoinColumn(name = "vehicleTypeId", nullable = false)
    private VehicleType vehicleType;

    @Temporal(TemporalType.DATE)
    @Column(name = "dateOfFirstRegistration", nullable = false, length = 30)
    private Date dateOfFirstRegistration;

    @Column(name = "lastMileage", nullable = false, length = 100)
    private int lastMileage;

    public Vehicle(){
    }

    public Vehicle(Owner owner, VehicleType vehicleType, Date dateOfFirstRegistration, int lastMileage) {
        this.owner = owner;
        this.vehicleType = vehicleType;
        this.dateOfFirstRegistration = dateOfFirstRegistration;
        this.lastMileage = lastMileage;
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    // toString


    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", owner=" + owner +
                ", vehicleType=" + vehicleType +
                ", dateOfFirstRegistration=" + dateOfFirstRegistration +
                ", lastMileage=" + lastMileage +
                '}';
    }
}
