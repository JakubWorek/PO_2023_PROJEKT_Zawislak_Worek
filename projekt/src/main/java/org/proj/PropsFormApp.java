package org.proj;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PropsFormApp extends AbstractApp {

    @Override
    public void configureStage(Stage primaryStage, VBox viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation Props form");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("propsForm.fxml"));
        VBox viewRoot;
        try {
            viewRoot = loader.load();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        configureStage(primaryStage, viewRoot);

        primaryStage.show();
    }
}
