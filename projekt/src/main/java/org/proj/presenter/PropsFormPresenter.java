package org.proj.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import org.proj.SimulationApp;
import org.proj.model.SimulationProps;
import org.proj.model.elements.EMoveStyle;
import org.proj.model.elements.EMutationStyle;
import org.proj.model.maps.EMapType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropsFormPresenter {
    @FXML
    private ComboBox mapStyleCBox;
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
    @FXML
    private TextField minMutations;
    @FXML
    private TextField maxMutations;

    SimulationProps simulationProps;

    @FXML
    public void initialize() {
        moveStyleCBox.getItems().removeAll(moveStyleCBox.getItems());
        moveStyleCBox.getItems().addAll("FULLY_PREDESTINED", "BACK_AND_FORTH");
        moveStyleCBox.getSelectionModel().select("FULLY_PREDESTINED");

        mapStyleCBox.getItems().removeAll(mapStyleCBox.getItems());
        mapStyleCBox.getItems().addAll("WATER", "GLOBE");
        mapStyleCBox.getSelectionModel().select("WATER");

        csvFlag.setSelected(false);
    }

    public void onNewSimulationClicked(ActionEvent actionEvent) {
        if (!csvFileName.isDisable()) {
            Pattern pattern = Pattern.compile("^.+\\.csv$");
            Matcher matcher = pattern.matcher(csvFileName.getText());
            if (!matcher.matches()) {
                throw new RuntimeException("Not valid csv file name!");
            }
        }
        EMoveStyle moveStyle = (moveStyleCBox.getValue() == "FULLY_PREDESTINED" ? EMoveStyle.FULLY_PREDESTINED : EMoveStyle.BACK_AND_FORTH);
        EMapType mapType = (mapStyleCBox.getValue() == "WATER" ? EMapType.WATER : EMapType.GLOBE);
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
                mapType,
                Integer.parseInt(genesCount.getText()),
                Integer.parseInt(energyToReproduce.getText()),
                Integer.parseInt(energyToPass.getText()),
                Integer.parseInt(moveEnergy.getText()),
                !csvFileName.isDisable(),
                csvFileName.getText(),
                Integer.parseInt(simulationStepTime.getText()),
                Integer.parseInt(minMutations.getText()),
                Integer.parseInt(maxMutations.getText())
        );

        simulationProps = props;
        SimulationApp app = new SimulationApp(props);
        app.run();
    }

    public void toggleCSVFlag(MouseEvent mouseEvent) {
        csvFileName.setDisable(!csvFileName.isDisable());
    }

    public void onOpenDialogClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CFG", "*.cfg"));
        File initialDirectory = new File("src/main/resources/configs/");
        fileChooser.setInitialDirectory(initialDirectory);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                List<String> lines = new ArrayList<>();
                Scanner myReader = new Scanner(selectedFile);
                while (myReader.hasNextLine()) {
                    lines.add(myReader.nextLine().strip());
                }
                myReader.close();
                startEnergy.setText(lines.get(0));
                maxEnergy.setText(lines.get(1));
                moveEnergy.setText(lines.get(2));
                genesCount.setText(lines.get(3));
                energyToReproduce.setText(lines.get(4));
                energyToPass.setText(lines.get(5));
                moveStyleCBox.getSelectionModel().select(lines.get(6));
                mapStyleCBox.getSelectionModel().select(lines.get(7));
                energyFromPlant.setText(lines.get(8));
                plantCount.setText(lines.get(9));
                spawnPlantPerDay.setText(lines.get(10));
                animalCount.setText(lines.get(11));
                simulationStepTime.setText(lines.get(12));
                mapWidth.setText(lines.get(13));
                mapHeight.setText(lines.get(14));
                minMutations.setText(lines.get(15));
                maxMutations.setText(lines.get(16));
            } catch (FileNotFoundException e) {
            }
        }
    }

    public void onSaveConfigClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CFG", "*.cfg"));
        File initialDirectory = new File("src/main/resources/configs/");
        fileChooser.setInitialDirectory(initialDirectory);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                writer.println(startEnergy.getText());
                writer.println(maxEnergy.getText());
                writer.println(moveEnergy.getText());
                writer.println(genesCount.getText());
                writer.println(energyToReproduce.getText());
                writer.println(energyToPass.getText());
                writer.println(moveStyleCBox.getValue());
                writer.println(mapStyleCBox.getValue());
                writer.println(energyFromPlant.getText());
                writer.println(plantCount.getText());
                writer.println(spawnPlantPerDay.getText());
                writer.println(animalCount.getText());
                writer.println(simulationStepTime.getText());
                writer.println(mapWidth.getText());
                writer.println(mapHeight.getText());
                writer.println(minMutations.getText());
                writer.println(maxMutations.getText());
                writer.close();
            } catch (IOException ex) { }
        }
    }
}