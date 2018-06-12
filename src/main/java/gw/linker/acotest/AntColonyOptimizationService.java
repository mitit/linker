package gw.linker.acotest;

import gw.linker.entity.Element;
import gw.linker.entity.Pcb;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AntColonyOptimizationService {

    private double c = 1.0;
    private double alpha = 1;
    private double beta = 5;
    private double evaporation = 0.5;
    private double Q = 500;
    private double antFactor = 0.8;
    private double randomFactor = 0.01;

    private int maxIterations = 1000;

    private int numberOfCities;
    private int numberOfAnts;
    private double graph[][];
    private double emcMatrix[][];
    private double functionalAttachmentMatrix[][];
    private double trails[][];
    private List<Ant> ants = new ArrayList<>();
    private Random random = new Random();
    private double probabilities[];

    private int currentIndex;

    private int[] bestTourOrder;
    private double bestTourLength;

    private List<Pcb> pcbs;
    private List<Element> elements;
    private List<Long[][]> pcbMatrices;

    public AntColonyOptimizationService(int noOfCities) {
        graph = generateGraph();
        emcMatrix = generateEmcMatrix();
        functionalAttachmentMatrix = generateFunctionalAttachmentMatrix();
        elements = initElements();
        pcbs = initPcbs();
        pcbMatrices = initPcbMatrices();

        numberOfCities = graph.length;
        numberOfAnts = (int) (numberOfCities * antFactor);

        trails = new double[numberOfCities][numberOfCities];
        probabilities = new double[numberOfCities];
        IntStream.range(0, numberOfAnts)
                .forEach(i -> ants.add(new Ant(numberOfCities)));
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

    public double[][] generateEmcMatrix() {
        double[][] emcMatrix = new double[5][5];
        emcMatrix[0][0] = 5;
        emcMatrix[0][1] = 5;
        emcMatrix[0][2] = 2;
        emcMatrix[0][3] = 1;
        emcMatrix[0][4] = 1;

        emcMatrix[1][0] = 5;
        emcMatrix[1][1] = 5;
        emcMatrix[1][2] = 1;
        emcMatrix[1][3] = 1;
        emcMatrix[1][4] = 1;

        emcMatrix[2][0] = 2;
        emcMatrix[2][1] = 1;
        emcMatrix[2][2] = 5;
        emcMatrix[2][3] = 3;
        emcMatrix[2][4] = 4;

        emcMatrix[3][0] = 1;
        emcMatrix[3][1] = 1;
        emcMatrix[3][2] = 3;
        emcMatrix[3][3] = 5;
        emcMatrix[3][4] = 4;

        emcMatrix[4][0] = 1;
        emcMatrix[4][1] = 1;
        emcMatrix[4][2] = 4;
        emcMatrix[4][3] = 4;
        emcMatrix[4][4] = 5;

        return emcMatrix;
    }

    public double[][] generateFunctionalAttachmentMatrix() {
        double[][] functionalAttachmentMatrix = new double[5][5];
        functionalAttachmentMatrix[0][0] = 1;
        functionalAttachmentMatrix[0][1] = 1;
        functionalAttachmentMatrix[0][2] = 0;
        functionalAttachmentMatrix[0][3] = 0;
        functionalAttachmentMatrix[0][4] = 0;

        functionalAttachmentMatrix[1][0] = 1;
        functionalAttachmentMatrix[1][1] = 1;
        functionalAttachmentMatrix[1][2] = 0;
        functionalAttachmentMatrix[1][3] = 0;
        functionalAttachmentMatrix[1][4] = 0;

        functionalAttachmentMatrix[2][0] = 0;
        functionalAttachmentMatrix[2][1] = 0;
        functionalAttachmentMatrix[2][2] = 1;
        functionalAttachmentMatrix[2][3] = 1;
        functionalAttachmentMatrix[2][4] = 1;

        functionalAttachmentMatrix[3][0] = 0;
        functionalAttachmentMatrix[3][1] = 0;
        functionalAttachmentMatrix[3][2] = 1;
        functionalAttachmentMatrix[3][3] = 1;
        functionalAttachmentMatrix[3][4] = 1;

        functionalAttachmentMatrix[4][0] = 0;
        functionalAttachmentMatrix[4][1] = 0;
        functionalAttachmentMatrix[4][2] = 1;
        functionalAttachmentMatrix[4][3] = 1;
        functionalAttachmentMatrix[4][4] = 1;

        return functionalAttachmentMatrix;
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

    public List<Long[][]> initPcbMatrices() {
        List<Long[][]> pcbMatrices = new ArrayList<>();

        IntStream.range(0, pcbs.size())
                .forEach(i -> {
                    Pcb pcb = pcbs.get(i);
                    Long[][] pcbMatrix = new Long[(int) pcb.getLength()][(int) pcb.getWidth()];
                    pcbMatrices.add(pcbMatrix);
                });

        return pcbMatrices;
    }

    public Map<Long, String> initElementsStartpoints() {
        HashMap<Long, String> map = new HashMap<>();

        map.put(0L, "0;0");
        map.put(1L, "0;8");
        map.put(2L, "0;0");
        map.put(3L, "4;0");
        map.put(4L, "0;4");

        return map;
    }

    public List<List<Element>> initElementsInPcbs() {
        List<List<Element>> lists = new ArrayList<>();

        List<Element> l1 = new ArrayList<>();
        l1.add(Element
                .builder()
                .id(0)
                .label("N1")
                .length(8)
                .width(6)
                .build());

        l1.add(Element
                .builder()
                .id(1)
                .label("N2")
                .length(8)
                .width(6)
                .build());

        List<Element> l2 = new ArrayList<>();
        l2.add(Element
                .builder()
                .id(2)
                .label("N3")
                .length(4)
                .width(4)
                .build());
        l2.add(Element
                .builder()
                .id(3)
                .label("N4")
                .length(4)
                .width(4)
                .build());
        l2.add(Element
                .builder()
                .id(4)
                .label("N5")
                .length(4)
                .width(4)
                .build());

        lists.add(l1);
        lists.add(l2);
        return lists;
    }

    /**
     * Generate initial solution
     */
    public double[][] generateRandomMatrix(int n) {
        double[][] randomMatrix = new double[n][n];
        IntStream.range(0, n)
                .forEach(i -> IntStream.range(0, n)
                        .forEach(j -> randomMatrix[i][j] = Math.abs(random.nextInt(100) + 1)));

        return randomMatrix;
    }

    /**
     * Perform ant optimization
     */
    public void startAntOptimization() {
        IntStream.rangeClosed(1, 1)
                .forEach(i -> {
                    System.out.println("Attempt #" + i);
                    solve();
                });
    }

    /**
     * Use this method to run the main logic
     */
    public int[] solve() {
        setupAnts();
        clearTrails();
        IntStream.range(0, maxIterations)
                .forEach(i -> {
                    moveAnts();
                    updateTrails();
                    updateBest();
                });
//        System.out.println("Best tour length: " + (bestTourLength - numberOfCities));
        System.out.println("Best tour length: " + (bestTourLength));
        System.out.println("Best tour order: " + Arrays.toString(bestTourOrder));

        IntStream.range(1, bestTourOrder.length)
                .forEach(i -> {
                    if (graph[bestTourOrder[i]][bestTourOrder[i - 1]] == 0.01)
                        System.out.println("Не подходит по пути");
                });
        IntStream.range(1, bestTourOrder.length)
                .forEach(i -> {
                    if (emcMatrix[bestTourOrder[i]][bestTourOrder[i - 1]] == 1)
                        System.out.println("Не подходит по ЭМС");
                });

        List<Element> sortedElements = new ArrayList<>();
        for (int i = 0; i < bestTourOrder.length; i++) {
            sortedElements.add(elements.get(bestTourOrder[i]));
        }


//        int currentPcbMatrix = 0;
//        int currentX = 0;
//        int currentY = 0;
//        int nextX = 0;
//        int nextY = 0;
//
//        for (int i = 0; i < sortedElements.size(); i++) {
//
//
//
//            Element element = sortedElements.get(i);
//            Long[][] pcbMatrix = pcbMatrices.get(currentPcbMatrix);
//
//            if (element.getWidth()  <=  pcbMatrix.length - currentX) {
//
//                if (element.getLength() <= pcbMatrix[0].length - currentY) {
//
//                    for (int m = currentX; m < currentX + element.getWidth(); m++)
//                        for (int n = currentY; n < currentY + element.getLength(); n++) {
//                            pcbMatrix[m][n] = (long) i;
//                        }
//
//                    currentX = (int) element.getWidth()  + currentX;
//                    if ((int) element.getLength()  + currentY > nextY) nextY = (int) element.getLength() + currentY;
//                } else {
//                    //TODO: переход на след пп
//                }
//            } else if (element.getLength()  <  pcbMatrix[0].length - nextY) {
//                currentX = 0;
//                currentY = nextY;
//
//                if (element.getWidth()  < currentX + pcbMatrix.length) {
//                    for (int m = currentX; m < currentX + element.getWidth(); m++)
//                        for (int n = 0; n < currentY + element.getLength(); n++) {
//                            pcbMatrix[m][n] = (long) i;
//                        }
//                }
//                currentX = (int) element.getWidth()  + currentX;
//                if ((int) element.getLength()  + currentY > nextY) nextY = (int) element.getLength()  + currentY;
//
//            } else {
//                return null;
//            }
//
//
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
//        }


        return bestTourOrder.clone();
    }

    /**
     * Prepare ants for the simulation
     */
    private void setupAnts() {
        IntStream.range(0, numberOfAnts)
                .forEach(i -> {
                    ants.forEach(ant -> {
                        ant.clear();
                        ant.visitCity(-1, random.nextInt(numberOfCities));
                    });
                });
        currentIndex = 0;
    }

    /**
     * At each iteration, move ants
     */
    private void moveAnts() {
        IntStream.range(currentIndex, numberOfCities - 1)
                .forEach(i -> {
                    ants.forEach(ant -> ant.visitCity(currentIndex, selectNextCity(ant)));
                    currentIndex++;
                });
    }

    /**
     * Select next city for each ant
     */
    private int selectNextCity(Ant ant) {
        int t = random.nextInt(numberOfCities - currentIndex);
        if (random.nextDouble() < randomFactor) {
            OptionalInt cityIndex = IntStream.range(0, numberOfCities)
                    .filter(i -> i == t && !ant.visited(i))
                    .findFirst();
            if (cityIndex.isPresent()) {
                return cityIndex.getAsInt();
            }
        }
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < numberOfCities; i++) {
            total += probabilities[i];
            if (total >= r) {
                return i;
            }
        }

        throw new RuntimeException("There are no other cities");
    }

    /**
     * Calculate the next city picks probabilites
     */
    public void calculateProbabilities(Ant ant) {
        int i = ant.trail[currentIndex];
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++) {
            if (!ant.visited(l)) {
                pheromone += Math.pow(trails[i][l], alpha) * Math.pow(graph[i][l], beta);
            }
        }
        for (int j = 0; j < numberOfCities; j++) {
            if (ant.visited(j)) {
                probabilities[j] = 0.0;
            } else {
                double numerator = Math.pow(trails[i][j], alpha) * Math.pow(graph[i][j], beta);
                probabilities[j] = numerator / pheromone;
            }
        }
    }

    /**
     * Update trails that ants used
     */
    private void updateTrails() {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                trails[i][j] *= evaporation;
            }
        }
        for (Ant a : ants) {
            double contribution = Q / a.trailLength(graph);
            for (int i = 0; i < numberOfCities - 1; i++) {
                trails[a.trail[i]][a.trail[i + 1]] += contribution;
            }
            trails[a.trail[numberOfCities - 1]][a.trail[0]] += contribution;
        }
    }

    /**
     * Update the best solution
     */
    private void updateBest() {
        if (bestTourOrder == null) {
            bestTourOrder = ants.get(0).trail;
            bestTourLength = ants.get(0)
                    .trailLength(graph);
        }
        for (Ant a : ants) {
            if (a.trailLength(graph) > bestTourLength) {
                bestTourLength = a.trailLength(graph);
                bestTourOrder = a.trail.clone();
            }
        }
    }

    /**
     * Clear trails after simulation
     */
    private void clearTrails() {
        IntStream.range(0, numberOfCities)
                .forEach(i -> {
                    IntStream.range(0, numberOfCities)
                            .forEach(j -> trails[i][j] = c);
                });
    }


}
