package com.traffic.controller;

import com.traffic.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Controller for the Welcome View.
 * Handles START button to transition to the Input View.
 */
public class WelcomeController {
    @FXML
    //Start Butonuna tıklanınca tetiklenir
    private void onEnterButtonClick(ActionEvent event) {
        //Butona tıklayan nesne alınır, ıklanan nesnenin ait olduğu sahne alınır
        //Bu sahne Stage’e dönüştürülür, yeni FXML dosyası (InputView.fxml) yüklenerek sahne değiştirilir
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.switchScene(stage, "/traffic/view/InputView.fxml");
    }
}