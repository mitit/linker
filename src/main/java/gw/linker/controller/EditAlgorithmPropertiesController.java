package gw.linker.controller;

import gw.linker.service.ProjectService;
import gw.linker.service.ProjectServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

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

    @Autowired
    private ProjectService projectService;

    @PostConstruct
    public void init() {
        weightParameter.setText(Double.toString(projectService.getBeta()));
        emcParameter.setText(Double.toString(projectService.getCcc()));
        funcParameter.setText(Double.toString(projectService.getDdd()));
        pcbSquareKoeffParameter.setText(Double.toString(projectService.getPcbSquareKoeffParameter()));
    }

    @FXML
    public void close() {
        stageController.setScene(SceneName.MAIN);
    }

    @FXML
    public void updateParameters() {
        projectService.setBeta(Double.parseDouble(weightParameter.getText()));
        projectService.setCcc(Double.parseDouble(emcParameter.getText()));
        projectService.setDdd(Double.parseDouble(funcParameter.getText()));
        projectService.setPcbSquareKoeffParameter(Double.parseDouble(pcbSquareKoeffParameter.getText()));
    }
}
