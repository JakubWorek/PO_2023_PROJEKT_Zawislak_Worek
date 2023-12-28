package org.proj;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.proj.model.SimulationProps;
import org.proj.presenter.SimulationPresenter;

import java.io.IOException;
import java.util.Objects;

public class SimulationApp extends Application implements Runnable {
    SimulationProps simulationProps;

    public SimulationApp(SimulationProps simulationProps) {
        this.simulationProps = simulationProps;
    }

    private void configureStage(Stage primaryStage, VBox viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
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

        SimulationPresenter controller = loader.getController();
        controller.setProps(simulationProps);

        primaryStage.show();
    }

    @Override
    public void run() {
        start(new Stage());
    }
}