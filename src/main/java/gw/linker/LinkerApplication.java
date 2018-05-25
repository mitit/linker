package gw.linker;

import gw.linker.controller.SceneName;
import gw.linker.controller.StageController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@Lazy
@SpringBootApplication
public class LinkerApplication extends AbstractSupportMain implements StageController {

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

    @Autowired
    private ControllersConfiguration controllersConfiguration;

    @Getter
    private Stage stage;

    private Scene mainScene;
    private Scene newProjectScene;
    private Scene openProjectScene;

    public static void main(String[] args) {
        launchApp(LinkerApplication.class, args);
    }


    @Override
    public void start(Stage stage) {
        setStage(stage);
        controllersConfiguration.setApplication(this);

        stage.setTitle(windowTitle);
        mainScene = new Scene(view.getView());
        stage.setScene(mainScene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setScene(SceneName name) {
        try {
            switch (name) {
                case MAIN:
                    if (mainScene == null) {
                        mainScene = new Scene(view.getView());
                    }
                    stage.setScene(mainScene);
                    stage.centerOnScreen();
                    break;

                case NEW_PROJECT:
                    if (newProjectScene == null) {
                        newProjectScene = new Scene(addElementView.getView());
                    }
                    stage.setScene(newProjectScene);
                    stage.centerOnScreen();
                    break;

                case OPEN_PROJECT:
                    if (openProjectScene == null) {
                        openProjectScene = new Scene(elementsView.getView());
                    }
                    stage.setScene(openProjectScene);
                    stage.centerOnScreen();
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
