package entities;

import javax.persistence.*;

@Entity
@Table(name = "InterventionType")
public class InterventionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public InterventionType() {
    }

    public InterventionType(String name) {
        this.name = name;
    }

   // getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InterventionType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}