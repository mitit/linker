package gw.linker.controller;

import javafx.fxml.FXML;

public class MainController extends BaseController {

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
}
