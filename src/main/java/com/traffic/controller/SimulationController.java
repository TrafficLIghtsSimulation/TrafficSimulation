package com.traffic.controller;

import com.traffic.model.ProportionalCalculationStrategy;
import com.traffic.model.TrafficData;
import com.traffic.model.TrafficStatistics;
import com.traffic.model.TrafficStatisticsEntry;
import com.traffic.model.enums.Direction;
import com.traffic.model.enums.LightState;
import com.traffic.model.interfaces.ISignalCalculationStrategy;
import com.traffic.util.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.*;

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
    // Işıkları kontrol etmek için Timeline kullanılabilir


    //Butonların tam işlevi
    @FXML private Circle northRedLight, northYellowLight, northGreenLight;
    @FXML private Circle southRedLight, southYellowLight, southGreenLight;
    @FXML private Circle eastRedLight, eastYellowLight, eastGreenLight;
    @FXML private Circle westRedLight, westYellowLight, westGreenLight;

    @FXML private Label eastTimerLabel;
    @FXML private Label westTimerLabel;
    @FXML private Label northTimerLabel;
    @FXML private Label southTimerLabel;

    @FXML private Button pauseButton, resumeButton, resetButton;

    @FXML private TableView<TrafficStatisticsEntry> statisticsTable;

    @FXML private TableColumn<TrafficStatisticsEntry, String> directionColumn;
    @FXML private TableColumn<TrafficStatisticsEntry, Number> passingColumn;
    @FXML private TableColumn<TrafficStatisticsEntry, Number> remainingColumn;

    private final Map<Direction, LightState> currentLightStates = new EnumMap<>(Direction.class);
    private final TrafficData trafficData = TrafficData.getInstance();
    private final TrafficStatistics trafficStatistics = TrafficStatistics.getInstance();

    private Timeline simulationTimeline;
    private List<Direction> directionOrder;
    private int currentIndex = 0;

    private final ISignalCalculationStrategy signalStrategy = new ProportionalCalculationStrategy();


    public void initialize() {
        setupTable();
        setupLightStates();
        setupTimeline();
        startGlobalSimulationTimer();  // Genel zamanlayıcı
    }

    private void startGlobalSimulationTimer() {
        simulationTimeline = new Timeline(new KeyFrame(Duration.seconds(120), e -> {
            System.out.println("120 saniyelik simülasyon süresi doldu. Simülasyon sona eriyor.");
            trafficStatistics.reset();
            trafficData.reset();

            // Tüm ışıkları kırmızı yap
            for (Direction d : Direction.values()) {
                currentLightStates.put(d, LightState.RED);
            }
            updateLightCircles();

            // Giriş ekranına dön
            Stage stage = (Stage) resetButton.getScene().getWindow();
            SceneManager.switchScene(stage, "/com/traffic/view/InputView.fxml");
        }));
        simulationTimeline.setCycleCount(1); // sadece bir kez çalışsın
        simulationTimeline.play();
    }


    private void setupTable() {
        directionColumn.setCellValueFactory(cellData -> cellData.getValue().directionProperty());
        passingColumn.setCellValueFactory(cellData -> cellData.getValue().passedProperty());
        remainingColumn.setCellValueFactory(cellData -> cellData.getValue().remainingProperty());

        statisticsTable.setItems(trafficStatistics.getObservableList());
    }

    private void setupLightStates() {
        for (Direction direction : Direction.values()) {
            currentLightStates.put(direction, LightState.RED);
        }

        updateLightCircles(); // Circle objelerini renklerle güncelle
    }

    private void updateLightCircles() {
        // Her yön için ışıkları kontrol et
        setLightColors(Direction.NORTH, northRedLight, northYellowLight, northGreenLight);
        setLightColors(Direction.SOUTH, southRedLight, southYellowLight, southGreenLight);
        setLightColors(Direction.EAST, eastRedLight, eastYellowLight, eastGreenLight);
        setLightColors(Direction.WEST, westRedLight, westYellowLight, westGreenLight);
    }

    //Timerlarla ışık sürelerinin geri sayımı
    private void setLightColors(Direction direction, Circle red, Circle yellow, Circle green) {
        LightState state = currentLightStates.get(direction);

        // Renkler: aktifse opak (1.0-ff ile ifade edilir), pasifse yarı saydam (0.4-66 ile ifade edilir)
        red.setFill(Paint.valueOf(state == LightState.RED ? "#ff0000ff" : "#ff000066"));
        yellow.setFill(Paint.valueOf(state == LightState.YELLOW ? "#fffb00ff" : "#fffb0066"));
        green.setFill(Paint.valueOf(state == LightState.GREEN ? "#00ff00ff" : "#00ff0066"));
    }
    private void startCountdownTimer(Direction direction, int durationInSeconds, Paint color) {
        Label label = getLabelForDirection(direction);
        label.setTextFill(color);

        Timeline countdown = new Timeline();
        for (int i = 0; i <= durationInSeconds; i++) {
            int timeLeft = durationInSeconds - i;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i), e -> label.setText(String.valueOf(timeLeft)));
            countdown.getKeyFrames().add(keyFrame);
        }

        countdown.play();
    }

    // Doğru Labelı bulmak için
    private Label getLabelForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> northTimerLabel;
            case SOUTH -> southTimerLabel;
            case EAST -> eastTimerLabel;
            case WEST -> westTimerLabel;
        };
    }

    //Işık sönünce label’ı sıfırla
    private void clearAllTimerLabels() {
        northTimerLabel.setText("");
        southTimerLabel.setText("");
        eastTimerLabel.setText("");
        westTimerLabel.setText("");
    }

    // Hangi yönün ışığının aktif olacağına karar verir.
    // Yeşil - Sarı - Kırmızı geçişlerini zamanlayarak yapar
    // SetupTimeline yalnızca ilk yönü başlatır

    //Daha sonra tekrar ele alınacak
    private void setupTimeline() {
        directionOrder = Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
        currentIndex = 0;

        // Yeşil ışık süresi hesaplamaları için strateji uygulanmalı
        Map<Direction, Integer> trafficCounts = trafficData.getAllCounts();

        boolean allZero = trafficCounts.values().stream().allMatch(count -> count == 0);

        if (allZero) {
            System.out.println("All directions have zero traffic. Simulation will not start.");
            return;
        }

        // İlk yönü bul (0 olmayan)
        while (trafficData.getVehicleCount(directionOrder.get(currentIndex)) == 0) {
            currentIndex = (currentIndex + 1) % directionOrder.size();
        }

        Direction start = directionOrder.get(currentIndex);
        activateDirection(start);
    }


    private void activateDirection(Direction direction) {
        Map<Direction, Integer> greenDurations = signalStrategy.calculateGreenLightDurations(trafficData.getAllCounts());
        int greenDuration = greenDurations.getOrDefault(direction, 0);
        int yellowDuration = 3;

        if (greenDuration == 0) {
            currentLightStates.put(direction, LightState.RED);
            updateLightCircles();
            proceedToNextDirection();
            return;
        }

        currentLightStates.put(direction, LightState.GREEN);
        updateLightCircles();

        // YEŞİL geri sayım başlasın
        startCountdownTimer(direction, greenDuration, Paint.valueOf("#00ff00"));

        // Araç geçişi varsayımı: her saniyede 1 araç geçebilir
        int totalVehicles = trafficData.getVehicleCount(direction);
        int estimatedPassing = Math.min(greenDuration, totalVehicles); // Örn: 8 sn yeşil varsa 8 araç geçsin

        // Modeli güncelle
        int remaining = totalVehicles - estimatedPassing;
        trafficData.setVehicleCount(direction, remaining);

        // Trafik istatistiklerini güncelle
        trafficStatistics.update(direction, estimatedPassing, remaining);

        // Sarı ışık geçişi için zamanlayıcı başlat
        Timeline yellowTimeline = new Timeline(new KeyFrame(Duration.seconds(greenDuration), e -> {
            currentLightStates.put(direction, LightState.YELLOW);
            updateLightCircles();
            startCountdownTimer(direction, yellowDuration, Paint.valueOf("#fffb00"));
        }));


        // Kırmızıya geçiş ve sıradaki yön için zamanlayıcı
        Timeline redTimeline = new Timeline(new KeyFrame(Duration.seconds(greenDuration + yellowDuration), e -> {
            currentLightStates.put(direction, LightState.RED);
            updateLightCircles();
            //startCountdownTimer(direction, yellowDuration, Paint.valueOf("#fffb00")); KIRMIZI IŞIĞI GERİ SAYIMI İÇİN DÜZENLENMELİ
            proceedToNextDirection();
        }));

        yellowTimeline.play();
        redTimeline.play();

    }

    private void proceedToNextDirection() {
        clearAllTimerLabels(); // önceki yönün sayacını sıfırla

        //Simülasyon bitiş kontrolü
        boolean allDone = Arrays.stream(Direction.values())
                .allMatch(d -> trafficData.getVehicleCount(d) == 0);

        if (allDone) {
            System.out.println("All vehicles processed. Simulation ends.");
            return;
        }

        currentIndex = (currentIndex + 1) % directionOrder.size();
        Direction next = directionOrder.get(currentIndex);

        if (trafficData.getVehicleCount(next) == 0) {
            activateDirection(next); // araç yoksa doğrudan geç
        } else {
            activateDirection(next);
        }
    }

    @FXML
    private void onPauseClick() {
        if (simulationTimeline != null) simulationTimeline.pause();
    }

    @FXML
    private void onResumeClick() {
        if (simulationTimeline != null) simulationTimeline.play();
    }

    @FXML
    private void onResetClick() {
        simulationTimeline.stop();
        trafficStatistics.reset();
        trafficData.reset();
        // tüm ışıkları kırmızıya çek
        for (Direction d : Direction.values()) {
            currentLightStates.put(d, LightState.RED);
        }
        updateLightCircles();
        // input ekranına yönlendir
        //Stage stage = (Stage) resetButton.getScene().getWindow();
        //ceneManager.switchScene(stage, "/com/traffic/view/InputView.fxml");
    }

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




