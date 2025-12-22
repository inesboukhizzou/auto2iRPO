package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "pricing")
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "intervention_type_id", nullable = false)
    private InterventionType interventionType;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", nullable = false)
    private VehicleType vehicleType;

    // Constructors
    public Pricing() {
    }

    public Pricing(double price, InterventionType interventionType, VehicleType vehicleType) {
        this.price = price;
        this.interventionType = interventionType;
        this.vehicleType = vehicleType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public InterventionType getInterventionType() {
        return interventionType;
    }

    public void setInterventionType(InterventionType interventionType) {
        this.interventionType = interventionType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return "Pricing{" +
                "id=" + id +
                ", price=" + price +
                ", interventionType=" + (interventionType != null ? interventionType.getName() : "N/A") +
                ", vehicleType=" + (vehicleType != null ? vehicleType.getBrand() + " " + vehicleType.getModel() : "N/A") +
                '}';
    }
}