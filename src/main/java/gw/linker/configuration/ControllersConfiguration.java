package gw.linker.configuration;

import gw.linker.LinkerApplication;
import gw.linker.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class ControllersConfiguration {

    private AppRef ref = new AppRef();

    @Bean(name = "mainView")
    public ViewHolder getMainView() throws IOException {
        return loadView("ui/main.fxml");
    }

    @Bean(name = "all-projects")
    public ViewHolder getAllProjectsView() throws IOException {
        return loadView("ui/all-projects.fxml");
    }

    @Bean(name = "add-project")
    public ViewHolder getAddProjectView() throws IOException {
        return loadView("ui/add-project.fxml");
    }

    @Bean
    public MainController getMainViewController() throws IOException {
        return (MainController) getMainView().getController();
    }

    @Bean
    public AllProjectsController getAllProjectsViewController() throws IOException {
        return (AllProjectsController) getAllProjectsView().getController();
    }

    @Bean
    public AddProjectController getAddProjectViewController() throws IOException {
        return (AddProjectController) getAddProjectView().getController();
    }

    public void setApplication(LinkerApplication app) {
        ref.app = app;
    }

    protected ViewHolder loadView(String url) throws IOException {
        try (InputStream fxmlStream = getClass().getClassLoader().getResourceAsStream(url)) {
            FXMLLoader loader = new FXMLLoader();
            loader.load(fxmlStream);
            BaseController controller = loader.getController();
            controller.setStageController(ref);
            return new ViewHolder(loader.getRoot(), controller);
        }
    }

    public class AppRef implements StageController {
        private LinkerApplication app;

        @Override
        public void setScene(SceneName scene) {
            app.setScene(scene);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class ViewHolder {
        private Parent view;
        private Object controller;
    }

}
