package org.proj;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class App {
    String args;

    public App() {
        args = "";
    }
    public App(String params) {
        args = params;
    }

    private void configureStage(Stage primaryStage, VBox viewRoot) {
        var scene = new Scene(viewRoot);
        if (!Objects.equals(args, "")) {
            // simulation
        }
        else {
            primaryStage.setScene(scene);
            primaryStage.setTitle("Simulation Props Form");
            primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
            primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
        }
    }

    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
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

    @Override
    public void run() {
        start(new Stage());
    }
}
