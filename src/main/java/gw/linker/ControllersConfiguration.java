package gw.linker;

import gw.linker.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    @Bean(name = "elements")
    public ViewHolder getElementsView() throws IOException {
        return loadView("ui/all-elements.fxml");
    }

    @Bean(name = "add-element")
    public ViewHolder getAddElementView() throws IOException {
        return loadView("ui/add-element.fxml");
    }

    @Bean
    public MainController getMainController() throws IOException {
        return (MainController) getMainView().getController();
    }

    @Bean
    public AllElementsController getSecondController() throws IOException {
        return (AllElementsController) getElementsView().getController();
    }

    @Bean
    public AddElementController getThirdController() throws IOException {
        return (AddElementController) getAddElementView().getController();
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

    public class ViewHolder {
        private Parent view;
        private Object controller;

        public ViewHolder(Parent view, Object controller) {
            this.view = view;
            this.controller = controller;
        }

        public Parent getView() {
            return view;
        }

        public void setView(Parent view) {
            this.view = view;
        }

        public Object getController() {
            return controller;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }
    }

}
