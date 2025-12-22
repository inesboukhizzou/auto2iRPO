package entities;
import jakarta.persistence.*;

@Entity
@Table(name="RepairType")
public class RepairType extends InterventionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public RepairType(){
    }
}
