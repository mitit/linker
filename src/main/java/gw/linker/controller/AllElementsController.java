package gw.linker.controller;

import gw.linker.entity.Element;
import gw.linker.service.ElementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

public class AllElementsController extends BaseController {

    @Autowired
    private ElementService elementService;
    @FXML
    private TableView<Element> table;

    private ObservableList<Element> data;

    @FXML
    public void initialize() {
    }

    @PostConstruct
    public void init() {
        List<Element> elements = elementService.findAll();
        data = FXCollections.observableArrayList(elements);

        TableColumn<Element, String> nrColumn = new TableColumn<>("Номер в проекте");
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("numberInProject"));

        TableColumn<Element, String> pcbNrColumn = new TableColumn<>("Номер ПП");
        pcbNrColumn.setCellValueFactory(new PropertyValueFactory<>("pcbInProjectNr"));

        TableColumn<Element, String> labelColumn = new TableColumn<>("Маркировка");
        labelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));

        TableColumn<Element, String> lengthColumn = new TableColumn<>("Длина");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn<Element, String> widthColumn = new TableColumn<>("Ширина");
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));

        TableColumn<Element, String> typeColumn = new TableColumn<>("Тип");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        table.getColumns().setAll(nrColumn, pcbNrColumn, labelColumn, lengthColumn, widthColumn, typeColumn);
        table.setItems(data);
    }

    @FXML
    public void close() {
        stageController.setScene(SceneName.MAIN);
    }
}