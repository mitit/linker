package gw.linker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditAlgorithmPropertiesController extends BaseController  {

    @FXML
    private TextField weightParameter;
    @FXML
    private TextField pheromoneParameter;
    @FXML
    private TextField emcParameter;
    @FXML
    private TextField funcParameter;
    @FXML
    private TextField pcbSquareKoeffParameter;

    @FXML
    public void init() {
    }

    @FXML
    public void close() {
        stageController.setScene(SceneName.MAIN);
    }

    @FXML
    public void updateParameters() {    }
}
