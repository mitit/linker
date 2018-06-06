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
        currentProject = Project
                .builder()
                .name("test")
                .elementList(initElements())
                .pcbList(initPcbs())
                .build();
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
                    Long[][] pcbMatrix = new Long[(int) pcb.getWidth()][(int) pcb.getLength()];//TODO: исправить на * 100
                    pcbMatrices.add(pcbMatrix);

                    elementsInPcbs.add(new ArrayList<>());
                });

        int currentPcbMatrix = 0;
        int currentX = 0;
        int currentY = 0;
        int lastY = 0;
        int nextX = 0;
        int nextY = 0;
        int lastX = 0;

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

                    currentX = (int) element.getWidth() + currentX;
//
                    if (lastY < currentY + element.getLength())
                        lastY = currentY + (int) element.getLength();
                } else {
                    //TODO: переход на след пп

                    if (currentPcbMatrix < pcbMatrices.size() - 1) {
                        currentPcbMatrix++;
                        i--;
                        currentX = 0;
                        currentY = 0;
                        lastY = 0;
                    } else {
                        break;
                    }
                }
            } else if (element.getLength() < pcbMatrix[0].length - lastY) {
                currentX = 0;
                currentY = lastY;

                if (element.getWidth() < pcbMatrix.length - currentX) {
                    for (int m = currentX; m < currentX + element.getWidth(); m++)
                        for (int n = 0; n < currentY + element.getLength(); n++) {
                            pcbMatrix[m][n] = (long) i;
                        }
                }
                elementsStartPoints.put(element, currentX + ";" + currentY);
                elementsInPcbs.get(currentPcbMatrix).add(element);

                currentX = (int) element.getWidth()  + currentX;
                if (lastY < currentY + element.getLength())
                    lastY = currentY + (int) element.getLength();


//                lastX = currentX;
//                if (lastX < currentX)
//                    lastX = currentX;

//                if ((int) element.getLength()  + currentY > nextY)
//                    nextY = (int) element.getLength()  + currentY;
//                currentY = 0;



            } else {

                if (currentPcbMatrix < pcbMatrices.size() - 1) {
                    currentPcbMatrix++;
                    i--;
                    currentX = 0;
                    currentY = 0;
                    lastY = 0;
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
                .graph(generateGraph())
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
                .length(5)
                .width(5)
                .build());

        elements.add(Element
                .builder()
                .id(3)
                .label("N4")
                .length(5)
                .width(5)
                .build());

        elements.add(Element
                .builder()
                .id(4)
                .label("N5")
                .length(5)
                .width(5)
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

    public double[][] generateGraph() {
        double[][] graph = new double[5][5];
        graph[0][0] = 0.01;
        graph[0][1] = 3;
        graph[0][2] = 1;
        graph[0][3] = 0.01;
        graph[0][4] = 0.01;

        graph[1][0] = 3;
        graph[1][1] = 0.01;
        graph[1][2] = 0.01;
        graph[1][3] = 0.01;
        graph[1][4] = 0.01;

        graph[2][0] = 1;
        graph[2][1] = 0.01;
        graph[2][2] = 0.01;
        graph[2][3] = 1;
        graph[2][4] = 2;

        graph[3][0] = 0.01;
        graph[3][1] = 0.01;
        graph[3][2] = 1;
        graph[3][3] = 0.01;
        graph[3][4] = 1;

        graph[4][0] = 0.01;
        graph[4][1] = 0.01;
        graph[4][2] = 2;
        graph[4][3] = 1;
        graph[4][4] = 0.01;

        return graph;
    }
}
