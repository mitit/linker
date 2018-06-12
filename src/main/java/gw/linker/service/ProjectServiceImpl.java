package gw.linker.service;

import gw.linker.entity.*;
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
                .elementList(TESTinitElements())
                .pcbList(TESTinitPcbs())
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
        int[] test = {1, 0, 2, 4, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13};
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
        int pcbPosition = 0;

        int nextX = 0;
        int nextY = 0;
        int lastX = 0;

        for (int i = 0; i < sortedElements.size(); i++) {

            Element element = sortedElements.get(i);
            Long[][] pcbMatrix = pcbMatrices.get(currentPcbMatrix);

            if (element.getWidth() <= pcbMatrix.length - currentX) {

                if (element.getLength() <= pcbMatrix[0].length - currentY) {

                    for (int m = currentX; m < currentX + element.getWidth(); m++)
                        for (int n = currentY; n < currentY + element.getLength(); n++) {
                            pcbMatrix[m][n] = (long) i;
                        }

                    int tmpX = 200 + currentX * 10 + pcbPosition;
                    elementsStartPoints.put(element, tmpX + ";" + currentY);
                    elementsInPcbs.get(currentPcbMatrix).add(element);

                    currentX = (int) element.getWidth() + currentX;
//
                    if (lastY < currentY + element.getLength())
                        lastY = currentY + (int) element.getLength();
                } else {
                    if (currentPcbMatrix < pcbMatrices.size() - 1) {
                        currentPcbMatrix++;
                        i--;
                        currentX = 0;
                        currentY = 0;
                        lastY = 0;
                        pcbPosition += pcbMatrix.length * 10 + 20;
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
                int tmpX = 200 + currentX * 10 + pcbPosition;
                elementsStartPoints.put(element, tmpX + ";" + currentY);
                elementsInPcbs.get(currentPcbMatrix).add(element);

                currentX = (int) element.getWidth() + currentX;
                if (lastY < currentY + element.getLength())
                    lastY = currentY + (int) element.getLength();

            } else {

                if (currentPcbMatrix < pcbMatrices.size() - 1) {
                    currentPcbMatrix++;
                    i--;
                    pcbPosition += pcbMatrix.length * 10 + 20;
                    currentX = 0;
                    currentY = 0;
                    lastY = 0;
                } else {
                    break;
                }
            }
        }


        return AcoAlgorithmResultDto
                .builder()
                .elementsInPcbs(elementsInPcbs)
                .elementsStartPoints(elementsStartPoints)
                .graph(TESTgenerateGraph())
                .build();
    }

    public List<Element> TESTinitElements() {
        List<Element> elements = new ArrayList<>();
        elements.add(Element
                .builder()
                .id(0)
                .label("R1")
                .length(8)
                .width(6)
                .build());

        elements.add(Element
                .builder()
                .id(1)
                .label("R2")
                .length(8)
                .width(6)
                .build());

        elements.add(Element
                .builder()
                .id(2)
                .label("R3")
                .length(5)
                .width(5)
                .build());

        elements.add(Element
                .builder()
                .id(3)
                .label("R4")
                .length(5)
                .width(5)
                .build());

        elements.add(Element
                .builder()
                .id(4)
                .label("R5")
                .length(5)
                .width(5)
                .build());

        elements.add(Element
                .builder()
                .id(5)
                .label("С1")
                .length(5)
                .width(4)
                .build());

        elements.add(Element
                .builder()
                .id(6)
                .label("С2")
                .length(4)
                .width(5)
                .build());

        elements.add(Element
                .builder()
                .id(7)
                .label("С3")
                .length(3)
                .width(3)
                .build());

        elements.add(Element
                .builder()
                .id(8)
                .label("R6")
                .length(6)
                .width(5)
                .build());

        elements.add(Element
                .builder()
                .id(9)
                .label("L5")
                .length(5)
                .width(3)
                .build());

        elements.add(Element
                .builder()
                .id(10)
                .label("L1")
                .length(5)
                .width(4)
                .build());

        elements.add(Element
                .builder()
                .id(11)
                .label("L2")
                .length(4)
                .width(4)
                .build());

        elements.add(Element
                .builder()
                .id(12)
                .label("L3")
                .length(3)
                .width(3)
                .build());

        elements.add(Element
                .builder()
                .id(13)
                .label("L4")
                .length(6)
                .width(4)
                .build());

        return elements;
    }

    public List<Pcb> TESTinitPcbs() {
        List<Pcb> pcbs = new ArrayList<>();

        pcbs.add(Pcb
                .builder()
                .id(0)
                .label("95-b0-e0-ER")
                .length(20)
                .width(10)
                .build());


        pcbs.add(Pcb
                .builder()
                .id(2)
                .label("78-b2-e0-ER")
                .length(20)
                .width(10)
                .build());


        pcbs.add(Pcb
                .builder()
                .id(1)
                .label("95-b1-e1-ER")
                .length(20)
                .width(10)
                .build());


        return pcbs;
    }


    public double[][] TESTgenerateGraph() {
        double[][] graph = new double[14][14];
        IntStream.range(0, 14).forEach(i -> {
            IntStream.range(0, 14).forEach(j -> {
                graph[i][j] = 0.01;
            });
        });

        graph[0][1] = 2;
        graph[1][0] = 2;
        graph[0][2] = 1;
        graph[2][0] = 1;
        graph[2][4] = 2;
        graph[4][2] = 2;
        graph[2][3] = 3;
        graph[3][2] = 3;
        graph[5][6] = 2;
        graph[6][5] = 2;
        graph[5][7] = 2;
        graph[7][5] = 2;
        graph[6][7] = 2;
        graph[7][6] = 2;
        graph[3][5] = 1;
        graph[5][3] = 1;
        graph[4][5] = 1;
        graph[5][4] = 1;
        graph[4][8] = 1;
        graph[8][4] = 1;
        graph[8][9] = 1;
        graph[9][8] = 1;
        graph[8][10] = 1;
        graph[10][8] = 1;
        graph[9][11] = 2;
        graph[11][9] = 2;
        graph[9][10] = 1;
        graph[10][9] = 1;
        graph[10][11] = 1;
        graph[11][10] = 1;
        graph[11][13] = 3;
        graph[13][11] = 3;
        graph[12][13] = 4;
        graph[13][12] = 4;
        graph[10][12] = 2;
        graph[12][10] = 2;
        graph[10][5] = 1;
        graph[5][10] = 1;


        return graph;
    }

    private double[][] generateMatrix(LinkSchema linkSchema) {
        List<Link> links = linkSchema.getLinks();
        List<Element> elementsInLinkSchema = new ArrayList<>();

        links.forEach(l -> {
            if (!elementsInLinkSchema.contains(l.getElement1()))
                elementsInLinkSchema.add(l.getElement1());
            if (!elementsInLinkSchema.contains(l.getElement2()))
                elementsInLinkSchema.add(l.getElement2());
        });

        int matrixSize = elementsInLinkSchema.size();
        double[][]  matrix = new double[matrixSize][matrixSize];

        IntStream.range(0, matrixSize).forEach(i -> {
            IntStream.range(0, matrixSize).forEach(j -> {
                matrix[i][j] = 0.01;
            });
        });

        links.forEach(l -> {
            int indexOfElement1InMatrix = elementsInLinkSchema.indexOf(l.getElement1());
            int indexOfElement2InMatrix = elementsInLinkSchema.indexOf(l.getElement2());

            if (matrix[indexOfElement1InMatrix][indexOfElement2InMatrix] == 0.01) {
                matrix[indexOfElement1InMatrix][indexOfElement2InMatrix] = 1;
                matrix[indexOfElement2InMatrix][indexOfElement1InMatrix] = 1;
            } else {
                matrix[indexOfElement1InMatrix][indexOfElement2InMatrix]++;
                matrix[indexOfElement2InMatrix][indexOfElement1InMatrix]++;
            }
        });

        return matrix;
    }
}
