package gw.linker.controller;

import gw.linker.entity.Project;
import gw.linker.service.ProjectService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;

public class AddProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @FXML
    private TextField projectName;

    @FXML
    public void initialize() {
    }

    @FXML
    public void saveProject() {
        projectService.save(Project
                .builder()
                .name(projectName.getText())
                .build());
    }

    @FXML
    public void close() {
        stageController.setScene(SceneName.MAIN);
    }
}
