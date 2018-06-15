package gw.linker.service;

import gw.linker.entity.*;
import gw.linker.entity.dto.AcoAlgorithmResultDto;
import gw.linker.repository.EmcRepository;
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
    private EmcRepository emcRepository;

    private double[][] ajacencyMatrix;
    private int[][] functionalMatrix;
    private int[][] emcMatrix;

    @Setter
    @Getter
    private double alpha = 1;
    @Setter
    @Getter
    private double beta = 1;
    @Setter
    @Getter
    private double ccc = 1;
    @Setter
    @Getter
    private double ddd = 1;
    @Setter
    @Getter
    private double pcbSquareKoeffParameter = 0.8;

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
        initData(currentProject.getLinkSchema());

        int[] acoAlgorithmIntArrayResult = runAcoAlgorithm();

        List<Element> acoAlgorithmElementListResult = Arrays
                .stream(acoAlgorithmIntArrayResult)
                .mapToObj(i -> currentProject.getElementList().get(i))
                .collect(Collectors.toList());

        AcoAlgorithmResultDto acoAlgorithmResultDto = startSolution(acoAlgorithmElementListResult);

        return acoAlgorithmResultDto;
    }

    private int[] runAcoAlgorithm() {
        AcoAlgorithmServiceImpl algorithm = new AcoAlgorithmServiceImpl(currentProject);
        algorithm.setEmcMatrix(emcMatrix);
        algorithm.setGraph(ajacencyMatrix);
        algorithm.setFunctionalAttachmentMatrix(functionalMatrix);
        algorithm.setAlpha(getAlpha());
        algorithm.setBeta(getBeta());
        algorithm.setCcc(getCcc());
        algorithm.setDdd(getDdd());

        int[] test = algorithm.startAntOptimization();

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
                .graph(ajacencyMatrix)
                .build();
    }

    private void initData(LinkSchema linkSchema) {
        List<Link> links = linkSchema.getLinks();
        List<Element> elementsInLinkSchema = new ArrayList<>();

        links.forEach(l -> {
            if (!elementsInLinkSchema.contains(l.getElement1()))
                elementsInLinkSchema.add(l.getElement1());
            if (!elementsInLinkSchema.contains(l.getElement2()))
                elementsInLinkSchema.add(l.getElement2());
        });

        int matrixSize = elementsInLinkSchema.size();
        double[][]  adjacencyM = new double[matrixSize][matrixSize];
        int[][]  emcM = new int[matrixSize][matrixSize];
        int[][]  funcM = new int[matrixSize][matrixSize];


        IntStream.range(0, matrixSize).forEach(i -> {
            IntStream.range(0, matrixSize).forEach(j -> {
                adjacencyM[i][j] = 0.01;
                adjacencyM[j][i] = 0.01;
                emcM[i][j] = 0;
                emcM[j][i] = 0;
                funcM[i][j] = 0;
                funcM[j][i] = 0;
            });
        });

        links.forEach(l -> {
            int indexOfElement1InMatrix = elementsInLinkSchema.indexOf(l.getElement1());
            int indexOfElement2InMatrix = elementsInLinkSchema.indexOf(l.getElement2());

            if (adjacencyM[indexOfElement1InMatrix][indexOfElement2InMatrix] == 0.01) {
                adjacencyM[indexOfElement1InMatrix][indexOfElement2InMatrix] = 1;
                adjacencyM[indexOfElement2InMatrix][indexOfElement1InMatrix] = 1;
            } else {
                adjacencyM[indexOfElement1InMatrix][indexOfElement2InMatrix]++;
                adjacencyM[indexOfElement2InMatrix][indexOfElement1InMatrix]++;
            }

            Emc emc = emcRepository.findByElement1AndElement2(l.getElement1(), l.getElement2());
            if (emc != null) {
                emcM[indexOfElement1InMatrix][indexOfElement2InMatrix] = emc.getValue();
                emcM[indexOfElement2InMatrix][indexOfElement1InMatrix] = emc.getValue();
            }

            if (l.getElement1().getFunctionalClass().equals(l.getElement2().getFunctionalClass())) {
                funcM[indexOfElement1InMatrix][indexOfElement2InMatrix] = 1;
                funcM[indexOfElement2InMatrix][indexOfElement1InMatrix] = 1;
            }
        });

        ajacencyMatrix = adjacencyM;
        emcMatrix = emcM;
        functionalMatrix = funcM;
        currentProject.setElementList(elementsInLinkSchema);
    }

}
