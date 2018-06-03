package gw.linker.controller;

import gw.linker.service.ProjectService;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;

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
    }
}
