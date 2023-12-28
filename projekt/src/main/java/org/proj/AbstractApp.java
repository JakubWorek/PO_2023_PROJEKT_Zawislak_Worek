package org.proj;

import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class AbstractApp extends Application implements Runnable {
    public abstract void configureStage(Stage primaryStage, VBox viewRoot);

    public abstract void start(Stage primaryStage);

    @Override
    public void run() {
        start(new Stage());
    }
}
