package gw.linker.entity;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Pcb {
    private long number;
    private Project project;
    private String label;
    private double width;
    private double length;
}
