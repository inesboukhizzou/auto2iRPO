package entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VehicleType")
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;
    @Column(name = "model", nullable = false, length = 100)
    private String model;
    @Column(name = "fuelType", nullable = false, length = 100)
    private String fuelType;
    @Column(name = "gearbox", nullable = false, length = 100)
    private String gearbox;
    @Column(name = "numberOfDoors", nullable = false, length = 100)
    private int numberOfDoors;
    @Column(name = "numberOfSeats", nullable = false, length = 100)
    private int numberOfSeats;
    @Column(name = "power", nullable = false, length = 100)
    private int power;

    @OneToMany(mappedBy = "vehicleType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();
    @OneToMany(mappedBy = "vehicleType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pricing> pricings = new ArrayList<>();

    public VehicleType() {
    }

    public VehicleType(String brand, String model, String fuelType, String gearbox, int numberOfDoors,
            int numberOfSeats, int power) {
        this.brand = brand;
        this.model = model;
        this.fuelType = fuelType;
        this.gearbox = gearbox;
        this.numberOfDoors = numberOfDoors;
        this.numberOfSeats = numberOfSeats;
        this.power = power;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setVehicleType(this);
    }

    
    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
        vehicle.setVehicleType(null);
    }

    public List<Pricing> getPricings() {
        return pricings;
    }

    public void setPricings(List<Pricing> pricings) {
        this.pricings = pricings;
    }

    
    public void addPricing(Pricing pricing) {
        pricings.add(pricing);
        pricing.setVehicleType(this);
    }

    
    public void removePricing(Pricing pricing) {
        pricings.remove(pricing);
        pricing.setVehicleType(null);
    }
}
