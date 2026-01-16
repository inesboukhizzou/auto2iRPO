package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Part")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interventionTypeId", nullable = false)
    private InterventionType interventionType;
    @Column(name = "name", nullable = false, length = 100)
    private String name;



    public Part() {
    }

    public Part(InterventionType interventionType, String name) {
        this.interventionType = interventionType;
        this.name = name;
    }



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

    public InterventionType getInterventionType() {
        return interventionType;
    }

    public void setInterventionType(InterventionType interventionType) {
        this.interventionType = interventionType;
    }

    @Override
    public String toString() {
        return "Part{" +
                "id=" + id +
                ", interventionType=" + interventionType +
                ", name='" + name + '\'' +
                '}';
    }
}
