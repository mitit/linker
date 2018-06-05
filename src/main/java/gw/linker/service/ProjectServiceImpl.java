package gw.linker.service;

import gw.linker.entity.Element;
import gw.linker.entity.Pcb;
import gw.linker.entity.Project;
import gw.linker.entity.dto.AcoAlgorithmResultDto;
import gw.linker.repository.ProjectRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private AcoAlgorithmService acoAlgorithmService;

    @Getter
    @Setter
    private Project currentProject;

    @Override
    public Project save(Project contact) {
        return projectRepository.save(contact);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<Project> find(String name) {
        return projectRepository.findById(name);
    }

    @Override
    @Transactional
    public AcoAlgorithmResultDto getWorkResult() {
        currentProject.setElementList(initElements());
        currentProject.setPcbList(initPcbs());
        int[] acoAlgorithmIntArrayResult = runAcoAlgorithm();

        List<Element> acoAlgorithmElementListResult = Arrays
                .stream(acoAlgorithmIntArrayResult)
                .mapToObj(i -> currentProject.getElementList().get(i))
                .collect(Collectors.toList());

        AcoAlgorithmResultDto acoAlgorithmResultDto = startSolution(acoAlgorithmElementListResult);

        return acoAlgorithmResultDto;
    }

    private int[] runAcoAlgorithm() {
        int[] test = {1, 0, 2, 4, 3};
        return test;
    }

    private AcoAlgorithmResultDto startSolution(List<Element> sortedElements) {
        Map<Element, String> elementsStartPoints = new HashMap<>();
        List<List<Element>> elementsInPcbs = new ArrayList<>();

        List<Long[][]> pcbMatrices = new ArrayList<>();
        IntStream.range(0, currentProject.getPcbList().size())
                .forEach(i -> {
                    Pcb pcb = currentProject.getPcbList().get(i);
                    Long[][] pcbMatrix = new Long[(int) pcb.getLength()][(int) pcb.getWidth()];//TODO: исправить на * 100
                    pcbMatrices.add(pcbMatrix);

                    elementsInPcbs.add(new ArrayList<>());
                });

        int currentPcbMatrix = 0;
        int currentX = 0;
        int currentY = 0;
        int nextX = 0;
        int nextY = 0;

        for (int i = 0; i < sortedElements.size(); i++) {

            Element element = sortedElements.get(i);
            Long[][] pcbMatrix = pcbMatrices.get(currentPcbMatrix);

            if (element.getWidth()  <=  pcbMatrix.length - currentX) {

                if (element.getLength() <= pcbMatrix[0].length - currentY) {

                    for (int m = currentX; m < currentX + element.getWidth(); m++)
                        for (int n = currentY; n < currentY + element.getLength(); n++) {
                            pcbMatrix[m][n] = (long) i;
                        }

                    elementsStartPoints.put(element, currentX + ";" + currentY);
                    elementsInPcbs.get(currentPcbMatrix).add(element);

                    currentX = (int) element.getWidth()  + currentX;
                    if ((int) element.getLength()  + currentY > nextY) nextY = (int) element.getLength() + currentY;
                } else {
                    //TODO: переход на след пп

                    if (currentPcbMatrix < pcbMatrices.size() - 1) {
                        currentPcbMatrix++;
                        i--;
                        currentX = 0;
                        currentY = 0;
                    } else {
                        break;
                    }
                }
            } else if (element.getLength()  <  pcbMatrix[0].length - nextY) {
                currentX = 0;
                currentY = nextY;

                if (element.getWidth()  < currentX + pcbMatrix.length) {
                    for (int m = currentX; m < currentX + element.getWidth(); m++)
                        for (int n = 0; n < currentY + element.getLength(); n++) {
                            pcbMatrix[m][n] = (long) i;
                        }
                }
                elementsStartPoints.put(element, currentX + ";" + currentY);
                elementsInPcbs.get(currentPcbMatrix).add(element);

                currentX = (int) element.getWidth()  + currentX;
                if ((int) element.getLength()  + currentY > nextY) nextY = (int) element.getLength()  + currentY;

            } else {

                if (currentPcbMatrix < pcbMatrices.size() - 1) {
                    currentPcbMatrix++;
                    i--;
                    currentX = 0;
                    currentY = 0;
                } else {
                    break;
                }

            }


//            for (Long[] arr :
//                    pcbMatrix) {
//                for (Long d :
//                        arr) {
//                    if (d != null) System.out.print(d);
//                    else System.out.print("-");
//                }
//                System.out.println();
//            }
//
//            System.out.println();
        }



        return AcoAlgorithmResultDto
                .builder()
                .elementsInPcbs(elementsInPcbs)
                .elementsStartPoints(elementsStartPoints)
                .build();
    }

    public List<Element> initElements() {
        List<Element> elements = new ArrayList<>();
        elements.add(Element
                .builder()
                .id(0)
                .label("N1")
                .length(8)
                .width(6)
                .build());

        elements.add(Element
                .builder()
                .id(1)
                .label("N2")
                .length(8)
                .width(6)
                .build());

        elements.add(Element
                .builder()
                .id(2)
                .label("N3")
                .length(4)
                .width(4)
                .build());

        elements.add(Element
                .builder()
                .id(3)
                .label("N4")
                .length(4)
                .width(4)
                .build());

        elements.add(Element
                .builder()
                .id(4)
                .label("N5")
                .length(4)
                .width(4)
                .build());

        return elements;
    }

    public List<Pcb> initPcbs() {
        List<Pcb> pcbs = new ArrayList<>();

        pcbs.add(Pcb
                .builder()
                .id(0)
                .label("FIRST")
                .length(20)
                .width(10)
                .build());

        pcbs.add(Pcb
                .builder()
                .id(1)
                .label("SECOND")
                .length(20)
                .width(10)
                .build());

        return pcbs;
    }
}
