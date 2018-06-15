package gw.linker.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
public class Pcb {
    @Id
    @Column
    private String label;
    @Column
    private double width;
    @Column
    private double length;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            mappedBy = "pcbList")
    private List<Project> projectList;
}
