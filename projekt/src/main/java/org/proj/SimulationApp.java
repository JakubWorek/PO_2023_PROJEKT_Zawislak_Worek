package org.proj;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.proj.model.SimulationProps;
import org.proj.presenter.SimulationPresenter;

import java.io.IOException;
import java.util.Objects;

public class SimulationApp extends Application implements Runnable {
    SimulationProps simulationProps;

    SimulationPresenter controller;

    public SimulationApp(SimulationProps simulationProps) {
        this.simulationProps = simulationProps;
    }

    private void configureStageV(Stage primaryStage, VBox viewRoot) {
        var scene = new Scene(viewRoot);

        scene.getStylesheets().add(getClass().getClassLoader().getResource("style.css").toExternalForm());

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

        configureStageV(primaryStage, viewRoot);

        controller = loader.getController();
        controller.setProps(simulationProps);

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        controller.shutdownSimulation();
    }

    @Override
    public void run() {
        start(new Stage());
    }
}