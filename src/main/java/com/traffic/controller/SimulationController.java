package com.traffic.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Controller for the simulation screen.
 * This class handles time counters and will later handle vehicle and light updates.
 */
public class SimulationController {
    // InputView'deki Start butonuna basınca SimulationView ekranı açılır ve simülasyon başlar
    // Pause : Nesnelerin X ve Y konumlarındaki değişim sıfır olmalı mevcut konumlarında kalacaklar, Timer mevcut durumda durucak, Işıkların durumlarında değişiklik olmamalı
    // Resume : Pause daki durum devam ettirilecek
    // Reset: Araçlar kaldırılacak, Timerlar sıfırlaacak, İstatsitikler sıfırlanacak, trafik ısıkları sönücek, InputView ekranına yönlendirecek
    // Araçların hareketi için animasyonlar(FadeTransition/PathTransition), hareket stratejileri,
    // Timeline kullanılabilir

    /*
    @FXML
    private Label timerLabel;

    private int time = 0;
    private Timeline timeline;

    @FXML
    public void initialize() {
        startTimer();
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            time++;
            timerLabel.setText("Time: " + time + " s");
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stopSimulation() {
        if (timeline != null) {
            timeline.stop();
        }
    } */
}
