package gw.linker.entity.dto;

import gw.linker.entity.Element;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class AcoAlgorithmResultDto {

    private Map<Element, String> elementsStartPoints;
    private List<List<Element>> elementsInPcbs;
    private double[][] graph;
}
