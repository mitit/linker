package gw.linker.controller;

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
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class MainController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @FXML
    private Label pcbCount;
    @FXML
    private Label elementCount;
    @FXML
    private Label pcbSquare;
    @FXML
    private Label elementSquare;
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

    @FXML
    public void editAlgorithmProperties() {
        stageController.setScene(SceneName.EDIT_ALGORITHM_PROPERTIES);
    }

    @FXML
    public void showUserguide() {
        stageController.setScene(SceneName.USERGUIDE);
    }

    public void openProject() {
        drawPcbs();
    }

    public void drawPcbs() {
        group.getChildren().clear();
        pcbCount.setText("Количество печатных плат: " + projectService.getCurrentProject().getPcbList().size());
        elementCount.setText("Количество элементов: " +  + projectService.getCurrentProject().getElementList().size());
        pcbSquare.setText("Суммарная площадь печатных плат: " + calculatePcbsSquare(projectService.getCurrentProject().getPcbList()));
        elementSquare.setText("Суммарная площадь элементов: " + calculateElementsSquare(projectService.getCurrentProject().getElementList()));

        double pcbPosition = 0.0;
        List<Pcb> pcbs = projectService.getCurrentProject().getPcbList();

        Rectangle rrr = new Rectangle(pcbPosition, 50, 200, 100);
        rrr.setStroke(Color.WHITESMOKE);
        rrr.setFill(null);
        rrr.setStrokeWidth(1);
        group.getChildren().addAll(rrr);

        pcbPosition += 200;

        for (int i = 0; i < pcbs.size(); i++) {
            Pcb pcb = pcbs.get(i);

            double pcbWidth = pcb.getWidth() * 10;
            double pcbLength = pcb.getLength() * 10;
            Rectangle r = new Rectangle(pcbPosition, 50, pcbWidth, pcbLength);
            r.setStroke(Color.BLACK);
            r.setFill(null);
            r.setStrokeWidth(1);

            Text text = new Text(pcb.getLabel());
            text.setY(45);
            text.setX(pcbPosition + 10);
            group.getChildren().addAll(r, text);
            pcbPosition += 20 + pcbWidth;
        }
    }

    @FXML
    public void startWork() {
        AcoAlgorithmResultDto acoAlgorithmResultDto = projectService.getWorkResult();

        List<Pcb> pcbs = projectService.getCurrentProject().getPcbList();
        List<Element> elements = projectService.getCurrentProject().getElementList();

        Map<Element, String> elementsStartPoints = acoAlgorithmResultDto.getElementsStartPoints();
        List<List<Element>> elementsInPcbs = acoAlgorithmResultDto.getElementsInPcbs();
        double[][] graph = acoAlgorithmResultDto.getGraph();

        for (int i = 0; i < pcbs.size(); i++) {

            List<Element> elementList = elementsInPcbs.get(i);
            for (int j = 0; j < elementList.size(); j++) {
                Element element = elementList.get(j);
                String point = elementsStartPoints.get(element);
                Double xPosition = Double.parseDouble(point.split(";")[0]);
                Double yPosition = 50 + (Double.parseDouble(point.split(";")[1]) * 10);

                double elementLength = element.getLength() * 10;
                double elementWidth = element.getWidth() * 10;
                Rectangle rectangle = new Rectangle(xPosition, yPosition, elementWidth, elementLength);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(null);
                rectangle.setStrokeWidth(1);

                Text elText = new Text(element.getLabel());
                elText.setX(xPosition + 10);
                elText.setY(yPosition + 10);

                group.getChildren().addAll(rectangle, elText);
            }

            for (int k = i; k < pcbs.size(); k++) {
                List<Element> tmpElementList = elementsInPcbs.get(k);

                IntStream.range(0, elementList.size()).forEach(m -> {
                    IntStream.range(0, tmpElementList.size()).forEach(n -> {
                        if (graph[(int) elementList.get(m).getId()][(int) tmpElementList.get(n).getId()] >= 1) {
                            System.out.println(elementList.get(m).getId() + " + " + tmpElementList.get(n).getId() + " = "
                                    + graph[(int) elementList.get(m).getId()][(int) tmpElementList.get(n).getId()]);

                            Element startElement = elementList.get(m);
                            Element endElement = tmpElementList.get(n);

                            String startPoint = elementsStartPoints.get(startElement);
                            String endPoint = elementsStartPoints.get(endElement);

                            Double startX = Double.parseDouble(startPoint.split(";")[0]) + startElement.getWidth() * 10 / 2;
                            Double startY = 50 + Double.parseDouble(startPoint.split(";")[1]) * 10 + startElement.getLength() * 10 / 2;

                            Double endX = Double.parseDouble(endPoint.split(";")[0]) + endElement.getWidth() * 10 / 2;
                            Double endY = 50 + Double.parseDouble(endPoint.split(";")[1]) * 10 + endElement.getLength() * 10 / 2;

                            Line line = new Line(startX, startY, endX, endY);
                            line.setStroke(Color.RED);

                            Text text = new Text((new Integer((int) graph[(int) elementList.get(m).getId()][(int) tmpElementList.get(n).getId()]).toString()));
                            if (startX > endX)
                                text.setX((startX + endX) / 2);
                            else
                                text.setX((endX + startX) / 2);

                            if (startY > endY)
                                text.setY((startY + endY) / 2);
                            else
                                text.setY((endY + startY) / 2);


                            group.getChildren().addAll(line, text);
                        }
                    });
                });


            }

        }

    }

    private double calculateElementsSquare(List<Element> elements) {
        double square = 0;
        for (Element element : elements) {
            square += element.getLength() * element.getWidth();
        }

        return square;
    }

    private double calculatePcbsSquare(List<Pcb> pcbs) {
        double square = 0;
        for (Pcb pcb : pcbs) {
            square += pcb.getLength() * pcb.getWidth();
        }

        return square;
    }
}
