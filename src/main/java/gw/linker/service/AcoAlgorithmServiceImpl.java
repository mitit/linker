package gw.linker.service;

import gw.linker.entity.Ant;
import gw.linker.entity.Element;
import gw.linker.entity.Pcb;
import gw.linker.entity.Project;

import java.util.*;
import java.util.stream.IntStream;

public class AcoAlgorithmServiceImpl implements AcoAlgorithmService {

    private double c = 1.0;
    private double alpha = 1;
    private double beta = 1;
    private double ccc = 1;
    private double ddd = 1;
    private double evaporation = 0.5;
    private double Q = 500;
    private double antFactor = 0.8;
    private double randomFactor = 0.01;

    private int maxIterations = 1000;

    private int numberOfCities;
    private int numberOfAnts;
    private double graph[][];
    private int emcMatrix[][];
    private int functionalAttachmentMatrix[][];
    private double trails[][];
    private List<Ant> ants = new ArrayList<>();
    private Random random = new Random();
    private double probabilities[];

    private int currentIndex;
    private int tmpF;

    private int[] bestTourOrder;
    private double bestTourLength;

    private List<Pcb> pcbs;
    private List<Element> elements;
    private List<Long[][]> pcbMatrices;

    public AcoAlgorithmServiceImpl(Project project) {
        elements = project.getElementList();
        pcbs = project.getPcbList();

        numberOfCities = graph.length;
        numberOfAnts = (int) (numberOfCities * antFactor);

        trails = new double[numberOfCities][numberOfCities];
        probabilities = new double[numberOfCities];
        IntStream.range(0, numberOfAnts)
                .forEach(i -> ants.add(new Ant(numberOfCities)));
    }

    public void setGraph(double[][] graph) {
        this.graph = graph;
    }

    public void setEmcMatrix(int[][] emcMatrix) {
        this.emcMatrix = emcMatrix;
    }

    public void setFunctionalAttachmentMatrix(int[][] functionalAttachmentMatrix) {
        this.functionalAttachmentMatrix = functionalAttachmentMatrix;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setCcc(double ccc) {
        this.ccc = ccc;
    }

    public void setDdd(double ddd) {
        this.ddd = ddd;
    }

    /**
     * Perform ant optimization
     */
    public int[] startAntOptimization() {
        IntStream.rangeClosed(1, 1)
                .forEach(i -> {
                    System.out.println("Attempt #" + i);

                });

        return solve();
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
        System.out.println("Best tour length: " + (bestTourLength));
        System.out.println("Best tour order: " + Arrays.toString(bestTourOrder));

        IntStream.range(1, bestTourOrder.length)
                .forEach(i -> {
                    if (graph[bestTourOrder[i]][bestTourOrder[i - 1]] == 0.01)
                        System.out.println("Не подходит по пути");
                });
        IntStream.range(1, bestTourOrder.length)
                .forEach(i -> {
                    if (emcMatrix[bestTourOrder[i]][bestTourOrder[i - 1]] == 0)
                        System.out.println("Не подходит по ЭМС");
                });

        List<Element> sortedElements = new ArrayList<>();
        for (int i = 0; i < bestTourOrder.length; i++) {
            sortedElements.add(elements.get(bestTourOrder[i]));
        }

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
                pheromone += (Math.pow(trails[i][l], alpha) + 1) * (Math.pow(graph[i][l], beta) + 1) *
                        (Math.pow(emcMatrix[i][l], ccc) + 1) * (Math.pow(functionalAttachmentMatrix[i][l], ddd) + 1);
            }
        }
        for (int j = 0; j < numberOfCities; j++) {
            if (ant.visited(j)) {
                probabilities[j] = 0.0;
            } else {
                double numerator = (Math.pow(trails[i][j], alpha) + 1) * (Math.pow(graph[i][j], beta) + 1) *
                        (Math.pow(emcMatrix[i][j], ccc) + 1) * (Math.pow(functionalAttachmentMatrix[i][j], ddd) + 1);
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


    private void calculateF() {
        int f = 0;
        int bound = bestTourOrder.length;
        int i = 1;
        while (i < bound) {
            f += graph[bestTourOrder[i]][bestTourOrder[i - 1]];
            f += emcMatrix[bestTourOrder[i]][bestTourOrder[i - 1]];
            f += functionalAttachmentMatrix[bestTourOrder[i]][bestTourOrder[i - 1]];
            i++;
        }

        if (currentIndex == 0 || f < tmpF) {
            tmpF = f;
        }
    }
}
