package gw.linker;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@Lazy
@SpringBootApplication
public class LinkerApplication extends Main {

    @Value("${ui.title:JavaFX приложение}")//
    private String windowTitle;

    @Qualifier("mainView")
    @Autowired
    private ControllersConfiguration.ViewHolder view;

    @Qualifier("elements")
    @Autowired
    private ControllersConfiguration.ViewHolder elementsView;

    @Qualifier("add-element")
    @Autowired
    private ControllersConfiguration.ViewHolder addElementView;

    @Override
    public void start(Stage stage) {
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(view.getView()));
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public void showAllElements() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Person");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setScene(new Scene(elementsView.getView()));

        dialogStage.showAndWait();
    }

    public void addElement() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add Person");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setScene(new Scene(addElementView.getView()));

        dialogStage.showAndWait();
    }


    public static void main(String[] args) {
        launchApp(LinkerApplication.class, args);
    }

}
