package gw.linker.controller;

import gw.linker.LinkerApplication;
import gw.linker.entity.ElementInProject;
import gw.linker.service.ElementInProjectService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;

public class MainController {

    @Autowired
    LinkerApplication linkerApplication;

    @Autowired
    private ElementInProjectService elementInProjectService;

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
    public void showAllElements() {
        linkerApplication.showAllElements();
    }

    @FXML
    public void addElement() {
        linkerApplication.addElement();
    }

//    @FXML
//    public void saveElement() {
//        Integer nrInProject = Integer.parseInt(txtNrInProject.getText());
//        Double length = Double.parseDouble(txtLength.getText());
//        Double width = Double.parseDouble(txtWidth.getText());
//
//        ElementInProject elementInProject = new ElementInProject();
//        elementInProject.setNumberInProject(nrInProject);
//        elementInProject.setLength(length);
//        elementInProject.setWidth(width);
//        elementInProjectService.save(elementInProject);
//    }
}
