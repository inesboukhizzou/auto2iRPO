package entities;

import jakarta.persistence.*;

/**
 * Represents a type of repair intervention.
 * Repairs are interventions done in response to incidents or damage,
 * as opposed to scheduled maintenance.
 * 
 * This class inherits name from InterventionType.
 * While it doesn't add specific fields, it serves as a type discriminator
 * for distinguishing repairs from maintenance in the database.
 */
@Entity
@Table(name = "RepairType")
public class RepairType extends InterventionType {

    public RepairType() {
        super();
    }

    public RepairType(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "RepairType{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
