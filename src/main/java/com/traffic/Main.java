package com.traffic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/traffic/view/WelcomeView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Traffic Light Simulation");

        // Application icon
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/traffic/view/main_assets/caricon.png")));

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}