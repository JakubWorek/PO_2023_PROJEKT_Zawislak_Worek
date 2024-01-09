package org.proj.presenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.proj.SimulationApp;
import org.proj.model.SimulationProps;
import org.proj.model.elements.Animal;
import org.proj.model.maps.AbstractWorldMap;
import org.proj.model.maps.GlobeMap;
import org.proj.utils.Vector2d;
import org.proj.Simulation;

import java.util.List;
import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class SimulationPresenter implements IMapChangeListener {

    @FXML
    private Label diedAt;
    @FXML
    private Label currMove;
    @FXML
    private Label genotype;
    @FXML
    private Label childrenCount;
    @FXML
    private Label plantsEaten;
    @FXML
    private Label daysLived;
    @FXML
    private Label energyLevel;
    @FXML
    private Button bestGrassBtn;
    @FXML
    private Button bestAnimalsBtn;
    @FXML
    private Label averageChildrenValue;
    @FXML
    private Label averageLifespanValue;
    @FXML
    private Label averageEnergyValue;
    @FXML
    private Label freeFieldsValue;
    @FXML
    private Label plantsValue;
    @FXML
    private Label animalsDeadValue;
    @FXML
    private Label animalsAliveValue;
    @FXML
    private Label mapHeightValue;
    @FXML
    private Label mapWidthValue;
    private SimulationProps simulationProps;
    private AbstractWorldMap worldMap;
    private Simulation simulation;

    @FXML
    private Button pauseToggleBtn;

    @FXML
    private GridPane grid;

    private Animal animalToFollow;

    Thread appThread;
    boolean running = false;

    public void setProps(SimulationProps simulationProps) {
        this.simulationProps = simulationProps;
        mapWidthValue.setText(simulationProps.getMapWidth().toString());
        mapHeightValue.setText(simulationProps.getMapHeight().toString());
    }

    private void clearGrid() {
        grid.getChildren().retainAll();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.setHgap(1);
        grid.setVgap(1);
    }

    public void drawMap() {
        clearGrid();
        int width =  simulationProps.getMapWidth();
        int height =  simulationProps.getMapHeight();
        int CELL = min((520-width)/width, (520-height)/height);

        int limit = max(height, width);

        for (int i = 0; i < limit; i++) {
            for (int j = 0; j < limit; j++) {
                VBox vbox = new VBox();
                if (worldMap.getForestedEquator().isPreferable(new Vector2d(i, simulationProps.getMapHeight()-j)))
                    vbox.getStyleClass().add("equator-cell");
                Label label = new Label("");
                Object object = worldMap.objectAt(new Vector2d(i, simulationProps.getMapHeight()-j-1));
                if (object != null)
                    label.setText(object.toString());
                label.setStyle("-fx-font-size: %d".formatted((int)(CELL/2.5)));
                vbox.getChildren().add(label);
                grid.add(vbox, i, j);
                vbox.setAlignment(Pos.CENTER);
                GridPane.setHalignment(label, HPos.CENTER);
            }
            grid.getRowConstraints().add(new RowConstraints(CELL));
            grid.getColumnConstraints().add(new ColumnConstraints(CELL));
        }
    }

    void updateSelectedAnimalStats() {
        if (animalToFollow == null) {
            energyLevel.setText("");
            daysLived.setText("");
            plantsEaten.setText("");
            childrenCount.setText("");
            currMove.setText("Gene index: # | Value: #");
            diedAt.setText("");
            genotype.setText("##########");
        }
        else {
            energyLevel.setText(animalToFollow.getEnergy().toString());
            daysLived.setText(animalToFollow.getAge().toString());
            plantsEaten.setText(animalToFollow.getPlantsEaten().toString());
            childrenCount.setText(animalToFollow.getChildrenMade().toString());
            currMove.setText("Gene index: " + animalToFollow.getGeneIndex().toString() + " | Value: " + animalToFollow.getGenome()[animalToFollow.getGeneIndex()]);
            if (animalToFollow.getEnergy() == 0) {
                diedAt.setText(animalToFollow.getDeathDate().toString());
            }
        }
    }

    public void onStartBtnClicked(ActionEvent actionEvent) {
        worldMap = new GlobeMap(simulationProps);

        clearGrid();

        Button btn = (Button) actionEvent.getSource();
        btn.setText("Reset");
        pauseToggleBtn.setText("Pause");
        bestAnimalsBtn.setVisible(false);
        bestGrassBtn.setVisible(false);
        animalToFollow = null;
        updateSelectedAnimalStats();
        animalsAliveValue.setText("");
        animalsDeadValue.setText("");
        plantsValue.setText("");
        freeFieldsValue.setText("");
        averageEnergyValue.setText("");
        averageLifespanValue.setText("");
        averageChildrenValue.setText("");

        worldMap.addListener(this);
        Simulation simulation = new Simulation(worldMap, simulationProps);
        this.simulation = simulation;
        if(running) appThread.stop();
        appThread = new Thread(simulation);
        appThread.start();
        running = true;
    }

    @Override
    public void mapChanged(AbstractWorldMap map, String message) {
        Platform.runLater(() -> {
            drawMap();
            animalsAliveValue.setText(map.getAliveAnimalsCount().toString());
            animalsDeadValue.setText(simulation.getDeadAnimalsCount().toString());
            plantsValue.setText(map.getPlantsCount().toString());
            freeFieldsValue.setText(map.getEmptyCount().toString());
            averageEnergyValue.setText(String.valueOf(simulation.getAvarageEnergy()));
            averageLifespanValue.setText(String.valueOf(simulation.getAverageLifeSpan()));
            averageChildrenValue.setText(String.valueOf(simulation.getAverageChildrenCount()));

            updateSelectedAnimalStats();
        });
    }

    public void clickGrid(javafx.scene.input.MouseEvent event) {
        if (Objects.equals(pauseToggleBtn.getText(), "Resume")) {
            Node clickedNode = event.getPickResult().getIntersectedNode();
            if (clickedNode != grid) {
                Node parent = clickedNode.getParent();
                while (GridPane.getColumnIndex(clickedNode) == null && parent != grid) {
                    clickedNode = parent;
                    parent = clickedNode.getParent();
                }
                Integer colIndex = GridPane.getColumnIndex(clickedNode);
                Integer rowIndex = GridPane.getRowIndex(clickedNode);
                if (worldMap.objectAt(new Vector2d(colIndex, simulationProps.getMapHeight() - rowIndex - 1)) instanceof Animal) {
                    animalToFollow = simulation.getAnimals().get(simulation.getAnimals().indexOf(worldMap.objectAt(new Vector2d(colIndex, simulationProps.getMapHeight() - rowIndex - 1))));
                    String genes = "";
                    for (int g : animalToFollow.getGenome())
                        genes += String.valueOf(g);
                    genotype.setText(genes);
                    updateSelectedAnimalStats();
                }
            }
        }
    }

    public void onPauseToggle(ActionEvent actionEvent) {
        simulation.togglePause();
        if (Objects.equals(pauseToggleBtn.getText(), "Pause")) {
            pauseToggleBtn.setText("Resume");
            bestAnimalsBtn.setVisible(true);
            bestGrassBtn.setVisible(true);
        }
        else {
            pauseToggleBtn.setText("Pause");
            bestAnimalsBtn.setVisible(false);
            bestGrassBtn.setVisible(false);
        }
    }
}
