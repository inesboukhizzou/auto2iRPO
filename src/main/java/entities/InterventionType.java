package entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "InterventionType")
public class InterventionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "interventionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Intervention> interventions = new ArrayList<>();

    @OneToMany(mappedBy = "interventionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Part> parts = new ArrayList<>();
    @OneToMany(mappedBy = "interventionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pricing> pricing = new ArrayList<>();

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

    public List<Intervention> getInterventions() {
        return interventions;
    }
    public void setInterventions(List<Intervention> interventions) {
        this.interventions = interventions;
    }
    public void addIntervention(Intervention intervention) {
        interventions.add(intervention);
        intervention.setInterventionType(this);
    }

    // Helper method to remove a vehicle
    public void removeIntervention(Intervention intervention) {
        interventions.remove(intervention);
        intervention.setInterventionType(null);
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
    public void addPart(Part part) {
        parts.add(part);
        part.setInterventionType(this);
    }

    // Helper method to remove a vehicle
    public void removePart(Part part) {
        parts.remove(part);
        part.setInterventionType(null);
    }

    @Override
    public String toString() {
        return "InterventionType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}