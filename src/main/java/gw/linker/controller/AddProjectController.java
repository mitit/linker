package gw.linker.controller;

import gw.linker.entity.LinkSchema;
import gw.linker.entity.Pcb;
import gw.linker.entity.Project;
import gw.linker.repository.LinkSchemaRepository;
import gw.linker.repository.PcbRepository;
import gw.linker.service.ProjectService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class AddProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private PcbRepository pcbRepository;
    @Autowired
    private LinkSchemaRepository linkSchemaRepository;
    @Autowired
    private MainController mainController;

    @FXML
    private TextField projectName;
    @FXML
    private TableView<Pcb> pcbTableView;
    @FXML
    private TableView<LinkSchema> linkSchemaTableView;

    private List<Pcb> pcbs;
    private List<LinkSchema> linkSchemas;

    @PostConstruct
    public void init() {
        pcbs = pcbRepository.findAll();
        linkSchemas = linkSchemaRepository.findAll();

        TableColumn<Pcb, String> pcbLabelColumn = new TableColumn<>("Маркировка");
        pcbLabelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));

        pcbTableView.getColumns().setAll(pcbLabelColumn);
        pcbTableView.setItems(FXCollections.observableArrayList(pcbs));
        pcbTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<LinkSchema, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        linkSchemaTableView.getColumns().setAll(nameColumn);
        linkSchemaTableView.setItems(FXCollections.observableArrayList(linkSchemas));
    }

    @FXML
    public void saveProject() {
        List<Pcb> pcbList = pcbTableView.getSelectionModel().getSelectedItems();
        LinkSchema linkSchema = linkSchemaTableView.getSelectionModel().getSelectedItem();

        Project project = Project
                .builder()
                .name(projectName.getText())
                .pcbList(pcbList)
                .linkSchema(linkSchema)
                .build();

        projectService.save(project);

        projectService.setCurrentProject(project);
        mainController.openProject();
        close();
    }

    @FXML
    public void close() {
        stageController.setScene(SceneName.MAIN);
    }


}
