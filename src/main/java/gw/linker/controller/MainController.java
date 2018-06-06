package gw.linker.controller;

import gw.linker.acotest.AntColonyOptimizationService;
import gw.linker.entity.Element;
import gw.linker.entity.Pcb;
import gw.linker.entity.dto.AcoAlgorithmResultDto;
import gw.linker.service.ProjectService;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class MainController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @FXML
    private Label projectName;
    @FXML
    private Label projectInfo;
    @FXML
    private Group group = new Group();

    @FXML
    public void initialize() {
    }

    @FXML
    public void showAllElements() {
        stageController.setScene(SceneName.ALL_PROJECTS);
    }

    @FXML
    public void addElement() {
        stageController.setScene(SceneName.NEW_PROJECT);
    }


    public void openProject() {
        projectName.setText(projectService.getCurrentProject().getName());
//        drawSolution();
    }

    @FXML
    public void startWork() {
        AcoAlgorithmResultDto acoAlgorithmResultDto = projectService.getWorkResult();
        double pcbPosition = 0.0;

        List<Pcb> pcbs = projectService.getCurrentProject().getPcbList();
        List<Element> elements = projectService.getCurrentProject().getElementList();

        Map<Element, String> elementsStartPoints = acoAlgorithmResultDto.getElementsStartPoints();
        List<List<Element>> elementsInPcbs = acoAlgorithmResultDto.getElementsInPcbs();
        double[][] graph = acoAlgorithmResultDto.getGraph();

        for (int i = 0; i < pcbs.size(); i++) {
            Pcb pcb = pcbs.get(i);

            //TODO: перевернуть
            double pcbWidth = pcb.getWidth() * 10;
            double pcbLength = pcb.getLength() * 10;
            Rectangle r = new Rectangle(pcbPosition, 50, pcbWidth, pcbLength);
            r.setStroke(Color.BLACK);
            r.setFill(null);
            r.setStrokeWidth(1);

            List<Element> elementList = elementsInPcbs.get(i);
            for (int j = 0; j < elementList.size(); j++) {
                Element element = elementList.get(j);
                String point = elementsStartPoints.get(element);
                Double xPosition = pcbPosition + (Double.parseDouble(point.split(";")[0]) * 10);
                Double yPosition = 50 + (Double.parseDouble(point.split(";")[1]) * 10);

                double elementLength = element.getLength() * 10;
                double elementWidth = element.getWidth() * 10;
                Rectangle rectangle = new Rectangle(xPosition, yPosition, elementWidth, elementLength);
//                System.out.println(xPosition + " = " + yPosition + " = " + elementWidth + " = " + elementLength);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(null);
                rectangle.setStrokeWidth(1);

                group.getChildren().addAll(rectangle);

//                if (j < elementList.size() - 1) {
//                    Element nextElement = elementList.get(j + 1);
//                    String nextPoint = elementsStartPoints.get(nextElement);
//                    Double nextXPosition = pcbPosition + (Double.parseDouble(nextPoint.split(";")[0]) * 10);
//                    Double nextYPosition = 50 + (Double.parseDouble(nextPoint.split(";")[1]) * 10);
//
//                    Double xStart = xPosition + elementWidth / 2;
//                    Double yStart = yPosition + elementLength / 2;
//                    Double xEnd = nextXPosition + nextElement.getWidth() * 10 / 2;
//                    Double yEnd = nextYPosition + nextElement.getLength() * 10 / 2;
//                    Line line = new Line(xStart, yStart, xEnd, yEnd);
//                    line.setAccessibleText("test");
//
//                    System.out.println(xStart + "_" + yStart + "_" + xEnd + "_" + yEnd);
//                    System.out.println();
//                    group.getChildren().addAll(line);
//                }
            }

            Double koeff = new Double(0.0);
            for (int k = i; k < pcbs.size(); k++) {
                List<Element> tmpElementList = elementsInPcbs.get(k);
                koeff +=  (20 * k + pcbs.get(k).getWidth());

                IntStream.range(0, elementList.size()).forEach(m -> {
                    IntStream.range(0, tmpElementList.size()).forEach(n -> {
                        if (graph[(int) elementList.get(m).getId()][(int) tmpElementList.get(n).getId()] >= 1) {
                            System.out.println(elementList.get(m).getId() + " + " + tmpElementList.get(n).getId() + " = "
                                    + graph[(int) elementList.get(m).getId()][(int) tmpElementList.get(n).getId()]);

                            Element startElement = elementList.get(m);
                            Element endElement = tmpElementList.get(n);

                            String startPoint = elementsStartPoints.get(startElement);
                            String endPoint = elementsStartPoints.get(endElement);

                            Double startX = Double.parseDouble(startPoint.split(";")[0]) * 10;
                            Double startY = Double.parseDouble(startPoint.split(";")[1]) * 10;

                            Double endX = Double.parseDouble(endPoint.split(";")[0]) * 10;
                            Double endY = Double.parseDouble(endPoint.split(";")[1]) * 10;

                            System.out.println(startX + ";" + startY + "=" + endX + ";" + endY);

                        }
                    });
                });
                System.out.println("koeff=" + koeff);

            }

            group.getChildren().addAll(r);
            pcbPosition += 20 + pcbWidth;
        }

    }

    public void drawSolution() {
        double pcbPosition = 0.0;

        Rectangle r1 = new Rectangle(50, 25, 100, 140);
        r1.setStroke(Color.BLACK);
        r1.setFill(null);
        r1.setStrokeWidth(1);

        AntColonyOptimizationService antColonyOptimizationService = new AntColonyOptimizationService(0);

        List<Pcb> pcbs = antColonyOptimizationService.initPcbs();
        List<Element> elements = antColonyOptimizationService.initElements();

        Map<Long, String> elementsStartPoints = antColonyOptimizationService.initElementsStartpoints();
        List<List<Element>> elementsInPcbs = antColonyOptimizationService.initElementsInPcbs();


        for (int i = 0; i < pcbs.size(); i++) {
            Pcb pcb = pcbs.get(i);

            double pcbWidth = pcb.getWidth() * 10;
            double pcbLength = pcb.getLength() * 10;
            Rectangle r = new Rectangle(pcbPosition, 50, pcbWidth, pcbLength);
            r.setStroke(Color.BLACK);
            r.setFill(null);
            r.setStrokeWidth(1);

            for (Element element : elementsInPcbs.get(i)) {
                String point = elementsStartPoints.get(element.getId());
                Double xPosition = pcbPosition + (Double.parseDouble(point.split(";")[0]) * 10);
                Double yPosition = 50 + (Double.parseDouble(point.split(";")[1]) * 10);

                double elementLength = element.getLength() * 10;
                double elementWidth = element.getWidth() * 10;
                Rectangle rectangle = new Rectangle(xPosition, yPosition, elementWidth, elementLength);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(null);
                rectangle.setStrokeWidth(1);
                group.getChildren().addAll(rectangle);
            }

            group.getChildren().addAll(r);
            pcbPosition += 20 + pcbWidth;
        }


    }
}
