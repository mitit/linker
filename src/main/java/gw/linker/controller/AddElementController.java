package gw.linker.controller;

import gw.linker.entity.Element;
import gw.linker.service.ElementService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;

public class AddElementController {

    @Autowired
    private ElementService elementService;

    @FXML
    private TextField txtNrInProject;
    @FXML
    private TextField txtPcbNrInProject;
    @FXML
    private TextField txtLabel;
    @FXML
    private TextField txtLength;
    @FXML
    private TextField txtWidth;
    @FXML
    private TextField txtType;

    @FXML
    public void initialize() {
    }

    @FXML
    public void saveElement() {
        Integer nrInProject = Integer.parseInt(txtNrInProject.getText());
        Double length = Double.parseDouble(txtLength.getText());
        Double width = Double.parseDouble(txtWidth.getText());

        Element element = new Element();
        element.setNumberInProject(nrInProject);
        element.setLength(length);
        element.setWidth(width);
        elementService.save(element);
    }
}
