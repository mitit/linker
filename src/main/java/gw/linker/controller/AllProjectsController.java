package gw.linker.controller;

import gw.linker.entity.Project;
import gw.linker.service.ProjectService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

public class AllProjectsController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @FXML
    private TableView<Project> projectTableView;
    @FXML
    public void initialize() {
    }

    @PostConstruct
    public void init() {
        List<Project> list = projectService.findAll();

        TableColumn<Project, String> nrColumn = new TableColumn<>("name");
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        projectTableView.getColumns().setAll(nrColumn);
        projectTableView.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    public void close() {
        stageController.setScene(SceneName.MAIN);
    }
}