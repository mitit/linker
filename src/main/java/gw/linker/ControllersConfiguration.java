package gw.linker;

import gw.linker.controller.MainController;
import gw.linker.controller.SecondController;
import gw.linker.controller.ThirdController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class ControllersConfiguration {

    @Bean(name = "mainView")
    public ViewHolder getMainView() throws IOException {
        return loadView("main.fxml");
    }

    @Bean(name = "elements")
    public ViewHolder getElementsView() throws IOException {
        return loadView("elements.fxml");
    }

    @Bean(name = "add-element")
    public ViewHolder getAddElementView() throws IOException {
        return loadView("add-element.fxml");
    }

    @Bean
    public MainController getMainController() throws IOException {
        return (MainController) getMainView().getController();
    }

    @Bean
    public SecondController getSecondController() throws IOException {
        return (SecondController) getElementsView().getController();
    }

    @Bean
    public ThirdController getThirdController() throws IOException {
        return (ThirdController) getAddElementView().getController();
    }

    protected ViewHolder loadView(String url) throws IOException {
        InputStream fxmlStream = null;
        try {
            fxmlStream = getClass().getClassLoader().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.load(fxmlStream);
            return new ViewHolder(loader.getRoot(), loader.getController());
        } finally {
            if (fxmlStream != null) {
                fxmlStream.close();
            }
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
