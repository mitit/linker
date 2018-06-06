package gw.linker;

import gw.linker.configuration.ControllersConfiguration;
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

    @Qualifier("all-projects")
    @Autowired
    private ControllersConfiguration.ViewHolder allProjectsView;

    @Qualifier("add-project")
    @Autowired
    private ControllersConfiguration.ViewHolder addProjectView;

    @Qualifier("edit-algorithm-properties")
    @Autowired
    private ControllersConfiguration.ViewHolder editAlgorithmPropertiesView;

    @Qualifier("program-description")
    @Autowired
    private ControllersConfiguration.ViewHolder programDescView;

    @Qualifier("userguide")
    @Autowired
    private ControllersConfiguration.ViewHolder userguideView;

    @Autowired
    private ControllersConfiguration controllersConfiguration;

    @Getter
    private Stage stage;

    private Scene mainScene;
    private Scene newProjectScene;
    private Scene openProjectScene;
    private Scene editAlgorithmPropertiesScene;
    private Scene programDescScene;
    private Scene userguideScene;

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
                        newProjectScene = new Scene(addProjectView.getView());
                    }
                    stage.setScene(newProjectScene);
                    stage.centerOnScreen();
                    break;

                case ALL_PROJECTS:
                    if (openProjectScene == null) {
                        openProjectScene = new Scene(allProjectsView.getView());
                    }
                    stage.setScene(openProjectScene);
                    stage.centerOnScreen();
                    break;

                case EDIT_ALGORITHM_PROPERTIES:
                    if (editAlgorithmPropertiesScene == null) {
                        editAlgorithmPropertiesScene = new Scene(editAlgorithmPropertiesView.getView());
                    }
                    stage.setScene(editAlgorithmPropertiesScene);
                    stage.centerOnScreen();
                    break;

                case USERGUIDE:
                    if (userguideScene == null) {
                        userguideScene = new Scene(userguideView.getView());
                    }
                    stage.setScene(userguideScene);
                    stage.centerOnScreen();
                    break;

                case PROGRAM_DESC:
                    if (programDescScene == null) {
                        programDescScene = new Scene(programDescView.getView());
                    }
                    stage.setScene(programDescScene);
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
