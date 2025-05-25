package com.traffic.controller;

import com.traffic.model.TrafficData;
import com.traffic.model.enums.Direction;
import com.traffic.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.util.Random;

/**
 * Controller for the input screen that handles manual and random input selections.
 */
public class InputViewController {
    @FXML private CheckBox manualCheckBox;
    @FXML private CheckBox randomCheckBox;

    @FXML private VBox manualInputPane;

    @FXML private TextField northField;
    @FXML private TextField southField;
    @FXML private TextField eastField;
    @FXML private TextField westField;

    @FXML private Button startButton;

    @FXML
    public void initialize() {
        // Başlangıçta manuel giriş paneli gizli, buton pasif

        manualInputPane.setVisible(false);
        manualInputPane.setManaged(false);
        startButton.setDisable(true);

        // TextField'lar değiştikçe kontrol et
        northField.textProperty().addListener((obs, oldVal, newVal) -> updateStartButton());
        southField.textProperty().addListener((obs, oldVal, newVal) -> updateStartButton());
        eastField.textProperty().addListener((obs, oldVal, newVal) -> updateStartButton());
        westField.textProperty().addListener((obs, oldVal, newVal) -> updateStartButton());
    }

    @FXML
    private void onManualSelected() {
        manualCheckBox.setSelected(true);
        randomCheckBox.setSelected(false);

        manualInputPane.setVisible(true);
        manualInputPane.setManaged(true);

        updateStartButton();
    }

    @FXML
    private void onRandomSelected() {
        randomCheckBox.setSelected(true);
        manualCheckBox.setSelected(false);

        manualInputPane.setVisible(false);
        manualInputPane.setManaged(false);

        startButton.setDisable(false);
    }

    @FXML
    private void onStartButtonClick(ActionEvent event) {
        TrafficData data = TrafficData.getInstance();

        if (manualCheckBox.isSelected()) {
            try {
                int north = Integer.parseInt(northField.getText());
                int south = Integer.parseInt(southField.getText());
                int east = Integer.parseInt(eastField.getText());
                int west = Integer.parseInt(westField.getText());

                data.setVehicleCount(Direction.NORTH, north);
                data.setVehicleCount(Direction.SOUTH, south);
                data.setVehicleCount(Direction.EAST, east);
                data.setVehicleCount(Direction.WEST, west);
                data.setRandom(false);

                System.out.println("Manual Traffic Counts:");
                System.out.println("North = " + north + ", South = " + south);
                System.out.println("East = " + east + ", West = " + west);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                SceneManager.switchScene(stage, "/traffic/view/SimulationView.fxml");

            } catch (NumberFormatException e) {
                showAlert("Invalid input! Please enter numbers only.");
            }

        } else if (randomCheckBox.isSelected()) {
            Random random = new Random();
            int north = random.nextInt(101);
            int south = random.nextInt(101);
            int east = random.nextInt(101);
            int west = random.nextInt(101);

            data.setVehicleCount(Direction.NORTH, north);
            data.setVehicleCount(Direction.SOUTH, south);
            data.setVehicleCount(Direction.EAST, east);
            data.setVehicleCount(Direction.WEST, west);
            data.setRandom(true);

            System.out.println("Random Traffic Counts:");
            System.out.println("North = " + north + ", South = " + south);
            System.out.println("East = " + east + ", West = " + west);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.switchScene(stage, "/traffic/view/SimulationView.fxml");
        }
    }


    private void updateStartButton() {
        if (manualCheckBox.isSelected()) {
            boolean allFilled = !northField.getText().isEmpty()
                    && !southField.getText().isEmpty()
                    && !eastField.getText().isEmpty()
                    && !westField.getText().isEmpty();
            startButton.setDisable(!allFilled);
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}



