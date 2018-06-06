package gw.linker.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
public class Emc {

    @Id
    @Column
    private Long id;
    @OneToOne
    private Element element1;
    @OneToOne
    private Element element2;
    @Column
    private int value;
}
