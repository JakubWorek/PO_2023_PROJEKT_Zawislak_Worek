package org.proj.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.proj.SimulationApp;
import org.proj.model.SimulationProps;
import org.proj.model.elements.EMoveStyle;
import org.proj.model.elements.EMutationStyle;

public class PropsFormPresenter {

    @FXML
    private TextField simulationStepTime;
    @FXML
    private TextField csvFileName;
    @FXML
    private CheckBox csvFlag;
    @FXML
    private TextField mapWidth;
    @FXML
    private TextField mapHeight;
    @FXML
    private TextField animalCount;
    @FXML
    private TextField plantCount;
    @FXML
    private TextField maxEnergy;
    @FXML
    private TextField moveEnergy;
    @FXML
    private TextField genesCount;
    @FXML
    private TextField spawnPlantPerDay;
    @FXML
    private TextField energyToReproduce;
    @FXML
    private TextField energyToPass;
    @FXML
    private ComboBox moveStyleCBox;
    @FXML
    private TextField energyFromPlant;
    @FXML
    private TextField startEnergy;

    SimulationProps simulationProps;

    @FXML
    public void initialize() {
        moveStyleCBox.getItems().removeAll(moveStyleCBox.getItems());
        moveStyleCBox.getItems().addAll("FULLY_PREDESTINED", "BACK_AND_FORTH");
        moveStyleCBox.getSelectionModel().select("FULLY_PREDESTINED");
        csvFlag.setSelected(false);
    }

    public void onNewSimulationClicked(ActionEvent actionEvent) {
        EMoveStyle moveStyle = (moveStyleCBox.getValue() == "FULLY_PREDESTINED" ? EMoveStyle.FULLY_PREDESTINED : EMoveStyle.BACK_AND_FORTH);
        SimulationProps props = new SimulationProps(
                Integer.parseInt(mapWidth.getText()),
                Integer.parseInt(mapHeight.getText()),
                (int) (Integer.parseInt(mapHeight.getText())*(0.2)),
                Integer.parseInt(animalCount.getText()),
                Integer.parseInt(plantCount.getText()),
                Integer.parseInt(spawnPlantPerDay.getText()),
                Integer.parseInt(startEnergy.getText()),
                Integer.parseInt(maxEnergy.getText()),
                Integer.parseInt(energyFromPlant.getText()),
                moveStyle,
                EMutationStyle.FULLY_RANDOM,
                Integer.parseInt(genesCount.getText()),
                Integer.parseInt(energyToReproduce.getText()),
                Integer.parseInt(energyToPass.getText()),
                Integer.parseInt(moveEnergy.getText()),
                !csvFileName.isDisable(),
                csvFileName.getText(),
                Integer.parseInt(simulationStepTime.getText())
        );

        simulationProps = props;
        SimulationApp app = new SimulationApp(props);
        app.run();
    }

    public void toggleCSVFlag(MouseEvent mouseEvent) {
        csvFileName.setDisable(!csvFileName.isDisable());
    }
}