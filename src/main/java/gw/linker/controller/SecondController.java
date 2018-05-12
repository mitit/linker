package gw.linker.controller;

import gw.linker.LinkerApplication;
import gw.linker.entity.ElementInProject;
import gw.linker.service.ElementInProjectService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

public class SecondController {



    @Autowired
    private ElementInProjectService elementInProjectService;

    @FXML
    private TableView<ElementInProject> table;


    private ObservableList<ElementInProject> data;

    @FXML
    public void initialize() {
    }

    @PostConstruct
    public void init() {
        List<ElementInProject> elements = elementInProjectService.findAll();
        data = FXCollections.observableArrayList(elements);

        TableColumn<ElementInProject, String> nrColumn = new TableColumn<>("Номер в проекте");
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("numberInProject"));

        TableColumn<ElementInProject, String> pcbNrColumn = new TableColumn<>("Номер ПП");
        pcbNrColumn.setCellValueFactory(new PropertyValueFactory<>("pcbInProjectNr"));

        TableColumn<ElementInProject, String> labelColumn = new TableColumn<>("Маркировка");
        labelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));

        TableColumn<ElementInProject, String> lengthColumn = new TableColumn<>("Длина");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn<ElementInProject, String> widthColumn = new TableColumn<>("Ширина");
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));

        TableColumn<ElementInProject, String> typeColumn = new TableColumn<>("Тип");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        table.getColumns().setAll(nrColumn, pcbNrColumn, labelColumn, lengthColumn, widthColumn, typeColumn);

        table.setItems(data);
    }





}