package entities;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "Vehicle")

public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ManyToOne relationship: multiple vehicles can belong to one owner
    @ManyToOne
    @JoinColumn(name = "ownerId", nullable = false)
    private Owner owner;

    // ManyToOne relationship: multiple vehicles can have the same type
    @ManyToOne
    @JoinColumn(name = "vehicleTypeId", nullable = false)
    private VehicleType vehicleType;

    @Temporal(TemporalType.DATE)
    @Column(name = "dateOfFirstRegistration", nullable = false, length = 30)
    private Date dateOfFirstRegistration;

    @Column(name = "lastMileage", nullable = false, length = 100)
    private int lastMileage;

    @OneToOne(optional = false)
    @JoinColumn(name = "registration_id", nullable = false)
    private Registration registration;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Intervention> interventions = new ArrayList<>();

    public Vehicle() {
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

    public List<Intervention> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<Intervention> interventions) {
        this.interventions = interventions;
    }

    public void addIntervention(Intervention intervention) {
        interventions.add(intervention);
        intervention.setVehicle(this);
    }

    // Helper method to remove an intervention
    public void removeIntervention(Intervention intervention) {
        interventions.remove(intervention);
        intervention.setVehicle(null);
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    // toString

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", registration="
                + (registration != null ? registration.getPart1() + registration.getPart2() + registration.getPart3()
                        : null)
                +
                ", lastMileage=" + lastMileage +
                ", dateOfFirstRegistration=" + dateOfFirstRegistration +
                '}';
    }
}
