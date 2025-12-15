package entities;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Intervention")
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false, length = 6)
    private Date date;

    @Column(name = "vehicleMileage", length = 100)
    private int vehicleMileage;

    @Column(name = "price", length = 6)
    private double price;

    public Intervention() {
    }

    public Intervention(Date date, int vehicleMileage, double price) {
        this.date = date;
        this.vehicleMileage = vehicleMileage;
        this.price = price;

    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getVehicleMileage() {
        return vehicleMileage;
    }

    public void setVehicleMileage(int vehicleMileage) {
        this.vehicleMileage = vehicleMileage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Intervention{" +
                "id=" + id +
                ", date=" + date +
                ", vehicleMileage=" + vehicleMileage +
                ", price=" + price +
                '}';
    }
}






