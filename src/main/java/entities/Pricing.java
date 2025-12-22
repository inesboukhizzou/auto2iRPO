package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Pricing")
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "price", nullable = false, length = 100)
    private double price;

    public Pricing() {
    }

    public Pricing(double price) {
        this.price = price;
    }

    // getters and setters

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

    @Override
    public String toString() {
        return "Pricing{" +
                "id=" + id +
                ", price=" + price +
                '}';
    }
}