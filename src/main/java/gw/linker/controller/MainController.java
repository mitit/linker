package gw.linker.controller;

import gw.linker.LinkerApplication;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

public class MainController extends BaseController {

    @Autowired
    LinkerApplication linkerApplication;


    @FXML
    public void initialize() {
    }

    @FXML
    public void showAllElements() {
        stageController.setScene(SceneName.OPEN_PROJECT);
    }

    @FXML
    public void addElement() {
        stageController.setScene(SceneName.NEW_PROJECT);
    }
}
