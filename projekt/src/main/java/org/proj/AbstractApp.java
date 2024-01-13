package org.proj;

import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class AbstractApp extends Application implements Runnable {
    public void configureStageV(Stage primaryStage, VBox viewRoot) {};
    public void configureStageH(Stage primaryStage, HBox viewRoot) {};

    public abstract void start(Stage primaryStage);

    @Override
    public void run() {
        start(new Stage());
    }
}
