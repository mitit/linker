package gw.linker.controller;

import gw.linker.entity.Pcb;
import gw.linker.entity.Project;
import gw.linker.exception.ProjectNotFoundException;
import gw.linker.service.ProjectService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class AllProjectsController extends BaseController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private MainController mainController;

    @FXML
    private TableView<Project> projectTableView;

    private List<Project> projects;

    @FXML
    public void initialize() {
    }

    @PostConstruct
    public void init() {
        projects = projectService.findAll();

        TableColumn<Project, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        projectTableView.getColumns().setAll(nameColumn);
        projectTableView.setItems(FXCollections.observableArrayList(projects));
    }

    @FXML
    public void close() {
        stageController.setScene(SceneName.MAIN);
    }

    @FXML
    public void updateProjectList() {
        projects = projectService.findAll();
        projectTableView.setItems(FXCollections.observableArrayList(projects));
    }

    @FXML
    public void selectProject() {
        String projectName = projectTableView.getSelectionModel().getSelectedItem().getName();
        Project project = projectService.find(projectName).orElseThrow(ProjectNotFoundException::new);
        project.setPcbList(initPcbs());
        projectService.setCurrentProject(project);
        mainController.openProject();
        close();
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
}