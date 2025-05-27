package com.traffic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.URL;

public class deneme_main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL location = deneme_main.class.getResource("/traffic/view/bos1.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
