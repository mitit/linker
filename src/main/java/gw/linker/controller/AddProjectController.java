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

    @FXML
    public void init() {
        pcbs = pcbRepository.findAll();
        linkSchemas = linkSchemaRepository.findAll();

        TableColumn<Pcb, String> pcbLabelColumn = new TableColumn<>("Маркировка");
        pcbLabelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));

        TableColumn<Pcb, String> pcbWidthColumn = new TableColumn<>("Ширина");
        pcbLabelColumn.setCellValueFactory(new PropertyValueFactory<>("width"));

        TableColumn<Pcb, String> pcbLengthColumn = new TableColumn<>("Длина");
        pcbLabelColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        pcbTableView.getColumns().setAll(pcbLabelColumn, pcbWidthColumn, pcbLengthColumn);
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


        List<Pcb> pcbTestList = initPcbs();

        Project project = Project
                .builder()
                .name(projectName.getText())
                .pcbList(pcbTestList)
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
