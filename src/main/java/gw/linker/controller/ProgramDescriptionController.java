package gw.linker.controller;

import javafx.fxml.FXML;

public class ProgramDescriptionController extends BaseController {

    @FXML
    public void init() {
    }

    @FXML
    public void close() {
        stageController.setScene(SceneName.MAIN);
    }
}
