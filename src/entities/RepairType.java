package entities;
import javax.persistence.*;

@Entity
@Table(name="RepairType")
public class RepairType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public RepairType(){
    }
}
