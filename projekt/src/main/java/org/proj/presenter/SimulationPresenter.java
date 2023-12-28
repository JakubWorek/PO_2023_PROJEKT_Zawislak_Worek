package org.proj.presenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.proj.SimulationApp;
import org.proj.model.SimulationProps;
import org.proj.model.maps.AbstractWorldMap;
import org.proj.model.maps.GlobeMap;
import org.proj.utils.Vector2d;
import org.proj.Simulation;

import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class SimulationPresenter implements IMapChangeListener {

    private SimulationProps simulationProps;
    private AbstractWorldMap worldMap;

    @FXML
    private GridPane grid;

    @FXML
    private Label currMoveLbl;

    @FXML
    public void initialize() {
    }

    public void setProps(SimulationProps simulationProps) {
        this.simulationProps = simulationProps;
    }

    private void clearGrid() {
        grid.getChildren().retainAll(grid.getChildren().get(0)); // hack to retain visible grid lines
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
    }

    public void drawMap() {
        clearGrid();
        int width =  simulationProps.getMapWidth();
        int height =  simulationProps.getMapHeight();
        int CELL = min(500/width, 500/height);

        int x = 0;
        int y = simulationProps.getMapHeight();

        int limit = max(height, width);

        for (int i = 0; i < limit; i++) {
            for (int j = 0; j < limit; j++) {
                Label label = new Label();

                if (i == 0 && j == 0) {
                    label.setText("y/x");
                    grid.getRowConstraints().add(new RowConstraints(CELL));
                }
                else if (i == 0) {
                    label.setText(""+y);
                    y--;
                }
                else if (j == 0) {
                    label.setText(""+x);
                    x++;
                    grid.getRowConstraints().add(new RowConstraints(CELL));
                }
                else {
                    System.out.println(new Vector2d(i, simulationProps.getMapHeight()-j-1));
                    Object object = worldMap.objectAt(new Vector2d(i, simulationProps.getMapHeight()-j));
                    if (object != null)
                        label.setText(object.toString());
                }
                grid.add(label, i, j);
                GridPane.setHalignment(label, HPos.CENTER);
            }
            grid.getColumnConstraints().add(new ColumnConstraints(CELL));
        }
    }

    public void onStartBtnClicked(ActionEvent actionEvent) {
        worldMap = new GlobeMap(simulationProps);

        clearGrid();

        System.out.println(simulationProps.getMapWidth());
        System.out.println(simulationProps.getMapHeight());

        int CELL = min(550/simulationProps.getMapWidth(), 550/simulationProps.getMapHeight());

        grid.getColumnConstraints().add(new ColumnConstraints(CELL));
        grid.getRowConstraints().add(new RowConstraints(CELL));

        worldMap.addListener(this);
        Simulation simulation = new Simulation(worldMap, simulationProps);
        Thread appThread = new Thread(simulation);
        appThread.start();
        //simulation.run();
    }

    @Override
    public void mapChanged(AbstractWorldMap map, String message) {
        Platform.runLater(() -> {
            drawMap();
            currMoveLbl.setText(message);
        });
    }
}
