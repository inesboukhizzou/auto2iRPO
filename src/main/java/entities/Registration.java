package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part1", nullable = false, length = 2)
    private String part1;

    @Column(name = "part2", nullable = false, length = 3)
    private int part2;

    @Column(name = "part3", length = 2)
    private String part3;

    @OneToOne(mappedBy = "registration")
    private Vehicle vehicle;

    public Registration(){}

    public Registration(String part1, int part2, String part3) {
        this.part1 = part1;
        this.part2 = part2;
        this.part3 = part3;

    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPart1() {
        return part1;
    }

    public void setPart1(String part1) {
        this.part1 = part1;
    }

    public int getPart2() {
        return part2;
    }

    public void setPart2(int part2) {
        this.part2 = part2;
    }

    public String getPart3() {
        return part3;
    }

    public void setPart3(String part3) {
        this.part3 = part3;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + id +
                ", part1='" + part1 + '\'' +
                ", part2=" + part2 +
                ", part3='" + part3 + '\'' +
                '}';
    }
}