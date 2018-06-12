package gw.linker.acotest;

public class StartAcoTest {

    public static void main(String[] args) {
        AntColonyOptimizationService antColony = new AntColonyOptimizationService(3);
        antColony.startAntOptimization();
    }
}
