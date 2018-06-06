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
public class Project {

    @Id
    @Column
    private String name;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    @JoinTable(name = "pcb_in_project",
            joinColumns = {@JoinColumn(name = "project_name")},
            inverseJoinColumns = {@JoinColumn(name = "pcb_id")})
    private List<Pcb> pcbList;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    @JoinTable(name = "element_in_project",
            joinColumns = {@JoinColumn(name = "project_name")},
            inverseJoinColumns = {@JoinColumn(name = "element_id")})
    private List<Element> elementList;

    @OneToOne
    private LinkSchema linkSchema;
}
