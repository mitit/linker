package gw.linker.controller;

import gw.linker.LinkerApplication;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

public class MainController {

    @Autowired
    LinkerApplication linkerApplication;

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
}
