package org.proj.presenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.proj.model.SimulationProps;
import org.proj.model.elements.Animal;
import org.proj.model.elements.FieldPaint;
import org.proj.model.elements.IWorldElement;
import org.proj.model.maps.AbstractWorldMap;
import org.proj.model.maps.GlobeMap;
import org.proj.model.maps.WaterMap;
import org.proj.utils.Vector2d;
import org.proj.Simulation;

import java.util.Objects;

import static java.lang.Math.min;

public class SimulationPresenter implements IMapChangeListener {
    @FXML
    private Label dayCounter;
    @FXML
    private Label mostPopularGenes;
    @FXML
    private Button saveBtn;
    @FXML
    private Label diedAt;
    @FXML
    private Label currMove;
    @FXML
    private Label genotype;
    @FXML
    private Label childrenCount;
    @FXML
    private Label descCount;
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
    @FXML
    private Button pauseToggleBtn;
    @FXML
    private GridPane grid;

    private SimulationProps simulationProps;
    private AbstractWorldMap worldMap;
    private Simulation simulation;

    private Animal animalToFollow;

    private Thread appThread;
    private boolean running = false;

    private Integer emptyFieldsCount = 0;

    private int CELL;

    private boolean showPreferableGrassFields = false;
    private boolean showMostPopularGenome = false;

    public void setProps(SimulationProps simulationProps) {
        this.simulationProps = simulationProps;
        mapWidthValue.setText(simulationProps.getMapWidth().toString());
        mapHeightValue.setText(simulationProps.getMapHeight().toString());
    }

    private void setupGrid() {
        grid.getChildren().retainAll();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.setHgap(1);
        grid.setVgap(1);

        int width =  simulationProps.getMapWidth();
        int height =  simulationProps.getMapHeight();
        CELL = min((520-width)/width, (520-height)/height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                VBox vbox = new VBox();
                vbox.setAlignment(Pos.CENTER);
                vbox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(4,4,4,4, false), new Insets(1,1,1,1))));
                Circle entity = new Circle(CELL/2.6, Color.TRANSPARENT);
                vbox.getChildren().add(entity);

                grid.add(vbox, i, j);
                if (i == 0) grid.getRowConstraints().add(new RowConstraints(CELL));
            }
            grid.getColumnConstraints().add(new ColumnConstraints(CELL));
        }
    }

    public void drawMap() {
        int width =  simulationProps.getMapWidth();
        int height =  simulationProps.getMapHeight();

        emptyFieldsCount = 0;

        int[] mostPopularGenotype = simulation.getMostPopularGenotype();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                VBox cell = (VBox) grid.getChildren().get(i * simulationProps.getMapHeight() + j);
                Circle entity = (Circle) cell.getChildren().get(0);

                IWorldElement object = worldMap.objectAt(new Vector2d(i, simulationProps.getMapHeight()-j-1));
                if (object != null) {
                    FieldPaint fp = object.getFieldPaint();
                    switch (object.getElementType()) {
                        case WATER -> {
                            entity.setVisible(false);
                            if (fp.backgroundFill() != cell.getBackground().getFills().get(0).getFill())
                                cell.setBackground(new Background(new BackgroundFill(fp.backgroundFill(), new CornerRadii(4,4,4,4, false), new Insets(1,1,1,1))));
                        }
                        case PLANT ->  {
                            entity.setVisible(true);
                            if (entity.getFill() != fp.entityColor())
                                entity.setFill(fp.entityColor());
                            if (showPreferableGrassFields && worldMap.getForestedEquator().isPreferable(new Vector2d(i, simulationProps.getMapHeight() - j)) && cell.getBackground().getFills().get(0).getFill() != Color.LIGHTGREEN)
                                cell.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(4, 4, 4, 4, false), new Insets(1, 1, 1, 1))));
                            else if (cell.getBackground().getFills().get(0).getFill() != Color.TRANSPARENT)
                                cell.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(4, 4, 4, 4, false), new Insets(1, 1, 1, 1))));
                        }
                        case ANIMAL -> {
                            entity.setVisible(true);
                            if (entity.getFill() != fp.entityColor())
                                entity.setFill(fp.entityColor());
                            Animal animal = (Animal)object;
                            if (animal == animalToFollow)
                                cell.setBackground(new Background(new BackgroundFill(Color.LIGHTSALMON, new CornerRadii(4, 4, 4, 4, false), new Insets(1, 1, 1, 1))));
                            else if (showMostPopularGenome && animal.getGenome() == mostPopularGenotype)
                                cell.setBackground(new Background(new BackgroundFill(Color.YELLOW, new CornerRadii(4, 4, 4, 4, false), new Insets(1, 1, 1, 1))));
                            else if (showPreferableGrassFields && worldMap.getForestedEquator().isPreferable(new Vector2d(i, simulationProps.getMapHeight() - j)) && cell.getBackground().getFills().get(0).getFill() != Color.LIGHTGREEN)
                                cell.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(4, 4, 4, 4, false), new Insets(1, 1, 1, 1))));
                            else if (cell.getBackground().getFills().get(0).getFill() != Color.TRANSPARENT)
                                cell.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(4, 4, 4, 4, false), new Insets(1, 1, 1, 1))));
                        }
                    }
                }
                else {
                    entity.setVisible(false);
                    if (cell.getBackground().getFills().get(0).getFill() != Color.TRANSPARENT)
                        cell.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(4,4,4,4, false), new Insets(1,1,1,1))));
                    emptyFieldsCount++;
                }

                if (showPreferableGrassFields && cell.getBackground().getFills().get(0).getFill() == Color.TRANSPARENT) {
                    if (worldMap.getForestedEquator().isPreferable(new Vector2d(i, simulationProps.getMapHeight() - j)) && cell.getBackground().getFills().get(0).getFill() != Color.LIGHTGREEN)
                        cell.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(4, 4, 4, 4, false), new Insets(1, 1, 1, 1))));
                }

            }
        }
    }

    void updateSelectedAnimalStats() {
        if (animalToFollow == null) {
            energyLevel.setText("");
            daysLived.setText("");
            plantsEaten.setText("");
            childrenCount.setText("");
            descCount.setText("");
            currMove.setText("Gene index: # | Value: #");
            diedAt.setText("");
            genotype.setText("##########");
        }
        else {
            energyLevel.setText(animalToFollow.getEnergy().toString());
            daysLived.setText(animalToFollow.getAge().toString());
            plantsEaten.setText(animalToFollow.getPlantsEaten().toString());
            childrenCount.setText(animalToFollow.getChildrenMade().toString());
            descCount.setText(animalToFollow.countDescendants().toString());
            currMove.setText("Gene index: " + animalToFollow.getGeneIndex().toString() + " | Value: " + animalToFollow.getGenome()[animalToFollow.getGeneIndex()]);
            if (animalToFollow.getEnergy() == 0 && animalToFollow.getDeathDate() > -1) {
                diedAt.setText(animalToFollow.getDeathDate().toString());
            }
        }
    }

    public void onStartBtnClicked(ActionEvent actionEvent) {
        switch (simulationProps.getMapType()) {
            case WATER -> worldMap = new WaterMap(simulationProps);
            case GLOBE -> worldMap = new GlobeMap(simulationProps);
        }

        setupGrid();

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
        mostPopularGenes.setText("");

        worldMap.addListener(this);
        Simulation simulation = new Simulation(worldMap, simulationProps);
        this.simulation = simulation;
        if(running) appThread.stop();
        appThread = new Thread(simulation);
        appThread.start();
        running = true;

        if (simulationProps.shouldSaveCSV()) {
            saveBtn.setVisible(true);
        }
    }

    @Override
    public void mapChanged(AbstractWorldMap map, String message) {
        Platform.runLater(() -> {
            drawMap();
            dayCounter.setText("Overall stats            | Day: " + simulationProps.getDaysElapsed().toString());
            animalsAliveValue.setText(simulation.getAliveAnimalsCount().toString());
            animalsDeadValue.setText(simulation.getDeadAnimalsCount().toString());
            plantsValue.setText(map.getPlantsCount().toString());
            freeFieldsValue.setText(emptyFieldsCount.toString());
            averageEnergyValue.setText(String.valueOf(simulation.getAverageEnergy()));
            String avgLifespan = "---";
            if (simulation.getDeadAnimalsCount() > 0) avgLifespan = String.valueOf(simulation.getAverageLifeSpan());
            averageLifespanValue.setText(avgLifespan);
            averageChildrenValue.setText(String.valueOf(simulation.getAverageChildrenCount()));
            mostPopularGenes.setText(simulation.getMostPopularGenotypeStr());

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
                    mapChanged(worldMap, "Highlight selected animals");
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

    public void onSaveBtnClicked(ActionEvent actionEvent) {
        simulation.getCsvLogger().SaveToFile();
    }

    public void shutdownSimulation() {
        if(running) appThread.stop();
    }

    public void onBestGrassClicked(ActionEvent actionEvent) {
        showPreferableGrassFields = !showPreferableGrassFields;
        mapChanged(worldMap, "Show preferable grass");
    }

    public void onBestGenomeClicked(ActionEvent actionEvent) {
        showMostPopularGenome = !showMostPopularGenome;
        mapChanged(worldMap, "Show best genome animals");
    }
}
