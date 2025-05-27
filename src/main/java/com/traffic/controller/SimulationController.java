package com.traffic.controller;

import com.traffic.model.*;
import com.traffic.model.enums.Direction;
import com.traffic.model.enums.LightState;
import com.traffic.model.interfaces.ISignalCalculationStrategy;
import com.traffic.util.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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

    @FXML private Button startButton, pauseButton, resumeButton, resetButton;

    @FXML private TableView<TrafficStatisticsEntry> statisticsTable;

    @FXML private TableColumn<TrafficStatisticsEntry, String> directionColumn;
    @FXML private TableColumn<TrafficStatisticsEntry, Number> passingColumn;
    @FXML private TableColumn<TrafficStatisticsEntry, Number> remainingColumn;

    @FXML
    private Pane vehicleLayer;

    private final Map<Direction, LightState> currentLightStates = new EnumMap<>(Direction.class);
    private final TrafficData trafficData = TrafficData.getInstance();
    private final TrafficStatistics trafficStatistics = TrafficStatistics.getInstance();
    private List<Direction> directionOrder;
    private int currentIndex = 0;

    private final ISignalCalculationStrategy signalStrategy = new ProportionalCalculationStrategy();

    // Her yön için sahnedeki araçların ImageView kuyruğu
    private final Map<Direction, Queue<ImageView>> vehicleImageQueues = new EnumMap<>(Direction.class);

    private static final double VEHICLE_STEP = 45.0;

    public void initialize() {
        setupTable();
        setupLightStates();
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
    private void setupTimeline() {
        directionOrder = Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
        currentIndex = 0;

        Map<Direction, Integer> trafficCounts = trafficData.getAllCounts();
        if (trafficCounts.values().stream().allMatch(count -> count == 0)){
            showAlert("All directions have zero traffic. Simulation will not start.");
            return;
        }

    }



    private void activateDirection(Direction direction) {
        Map<Direction, Integer> greenDurations = signalStrategy.calculateGreenLightDurations(trafficData.getAllCounts());
        int greenDuration = greenDurations.getOrDefault(direction, 0);
        int yellowDuration = 3;
        int totalCycle = greenDurations.values().stream().mapToInt(Integer::intValue).sum() + yellowDuration * 4;
        int redDuration = Math.max(0,totalCycle - greenDuration- yellowDuration);//dinamik kırmızı süresi

        if (greenDuration == 0) {
            currentLightStates.put(direction, LightState.RED);
            updateLightCircles();
            proceedToNextDirection();
            return;
        }

        currentLightStates.put(direction, LightState.GREEN);
        updateLightCircles();
        startCountdownTimer(direction, greenDuration, Paint.valueOf("#007200"));// YEŞİL geri sayım başlasın
        Timeline vehicleTimeline = new Timeline();
        int estimatedPassing = Math.min(greenDuration, vehicleImageQueues.getOrDefault(direction, new LinkedList<>()).size());
        for (int i = 0; i < estimatedPassing; i++) {
            int delay = i;
            KeyFrame frame = new KeyFrame(Duration.seconds(delay * 1.1), e -> animateVehicle(direction));
            vehicleTimeline.getKeyFrames().add(frame);
        }
        vehicleTimeline.play();


        // Araç geçişi varsayımı: her saniyede 1 araç geçebilir
        int totalVehicles = trafficData.getVehicleCount(direction);
        int remaining = totalVehicles - estimatedPassing;   // Modeli güncelle

        trafficData.setVehicleCount(direction, remaining);
        // Trafik istatistiklerini güncelle
        trafficStatistics.update(direction, estimatedPassing, remaining);

        // Sarı ışık geçişi için zamanlayıcı başlat
        Timeline yellowTimeline = new Timeline(new KeyFrame(Duration.seconds(greenDuration), e -> {
            currentLightStates.put(direction, LightState.YELLOW);
            updateLightCircles();
            startCountdownTimer(direction, yellowDuration, Paint.valueOf("#edc531"));
        }));


        // Kırmızıya geçiş ve sıradaki yön için zamanlayıcı
        Timeline redTimeline = new Timeline(new KeyFrame(Duration.seconds(greenDuration + yellowDuration), e -> {
            currentLightStates.put(direction, LightState.RED);
            updateLightCircles();
            startCountdownTimer(direction, redDuration, Paint.valueOf("#9d0208"));
        }));

        // SARIdan sonra diğer yön başlasın:
        Timeline nextDirectionTimeline = new Timeline(new KeyFrame(Duration.seconds(greenDuration + yellowDuration), e -> {
            proceedToNextDirection();
        }));

        yellowTimeline.play();
        redTimeline.play();
        nextDirectionTimeline.play();
    }

    private void proceedToNextDirection() {
        clearAllTimerLabels(); // önceki yönün sayacını sıfırla

        //Simülasyon bitiş kontrolü
        boolean allDone = Arrays.stream(Direction.values())
                .allMatch(d -> trafficData.getVehicleCount(d) == 0);

        if (allDone) {
            System.out.println("All vehicles processed. Simulation ends.");
            showAlert("Simulation finished. All vehicles have passed.");
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

    private ImageView createVehicleImage(String type, double x, double y, Direction direction) {
        String imagePath = switch (type) {
            case "car" -> "/com/traffic/view/main_assets/car.png";
            case "bus" -> "/com/traffic/view/main_assets/bus.png";
            case "truck" -> "/com/traffic/view/main_assets/truck.png";
            case "motorcycle" -> "/com/traffic/view/main_assets/motorcycle.png";
            default -> null;
        };

        if (imagePath == null) return null;

        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);
            imageView.setLayoutX(x);
            imageView.setLayoutY(y);

            //  Boyutları küçültüyoruz
            imageView.setFitWidth(28);
            imageView.setFitHeight(30);
            imageView.setPreserveRatio(true);

            // Yönüne göre döndür
            double angle = switch (direction) {
                case NORTH -> 180;
                case SOUTH -> 0;
                case EAST  -> 270;
                case WEST  -> 90;
            };
            imageView.setRotate(angle);

            return imageView;
        } catch (Exception e) {
            System.out.println("Image not loaded: " + imagePath);
            e.printStackTrace();
            return null;
        }
    }


    private double getVehicleX(Direction direction, int index) {
        return switch (direction) {
            case NORTH, SOUTH -> direction == Direction.NORTH ? 310 : 380;
            case EAST -> 485 + index * 40;
            case WEST -> 200 - index * 40;
        };
    }

    private double getVehicleY(Direction direction, int index) {
        return switch (direction) {
            case EAST, WEST -> direction == Direction.EAST ? 225 : 285;
            case NORTH -> 120 - index * 40;
            case SOUTH -> 380 + index * 40;
        };
    }

    private int getCurrentVehicleIndex(Direction direction) {
        int maxIndex = -1;
        for (javafx.scene.Node node : vehicleLayer.getChildren()) {
            if (node instanceof ImageView imageView) {
                double x = imageView.getLayoutX();
                double y = imageView.getLayoutY();
                double angle = imageView.getRotate();

                switch (direction) {
                    case NORTH -> {
                        if (x == 310 && angle == 180)
                            maxIndex++;
                    }
                    case SOUTH -> {
                        if (x == 380 && angle == 0)
                            maxIndex++;
                    }
                    case EAST -> {
                        if (y == 225 && angle == 270)
                            maxIndex++;
                    }
                    case WEST -> {
                        if (y == 285 && angle == 90)
                            maxIndex++;
                    }
                }
            }
        }
        return maxIndex + 1;
    }

    private void animateVehicle(Direction direction) {
        Queue<ImageView> queue = vehicleImageQueues.get(direction);
        if (queue == null || queue.isEmpty()) return;

        ImageView vehicle = queue.poll(); // sıradaki ilk aracı çıkar
        if (vehicle == null) return;

        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), vehicle);

        switch (direction) {
            case NORTH -> transition.setByY(VEHICLE_STEP);  // Aşağı (yukarıdan gelen)
            case SOUTH -> transition.setByY(-VEHICLE_STEP); // Yukarı (aşağıdan gelen)
            case EAST  -> transition.setByX(-VEHICLE_STEP); // Sola (sağdan gelen)
            case WEST  -> transition.setByX(VEHICLE_STEP);  // Sağa (soldan gelen)
        }

        transition.setOnFinished(e -> {
            vehicleLayer.getChildren().remove(vehicle); // Geçen aracı sahneden sil

            if (!queue.isEmpty()) {
                ImageView next = queue.peek(); // Sıradaki araç
                int index = getCurrentVehicleIndex(direction); // Şu an sahnedeki araç sayısı
                double x = getVehicleX(direction, index);
                double y = getVehicleY(direction, index);

                next.setLayoutX(x);
                next.setLayoutY(y);

                if (!vehicleLayer.getChildren().contains(next)) {
                    vehicleLayer.getChildren().add(next);
                    queue.poll(); // Gerçekten sıradan çıkar
                }
            }
        });

        transition.play();
    }

    @FXML
    private void onStartSimulationClick() {
        setupTimeline(); // Start butonuna basılınca simülasyon başlasın
        directionOrder = Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
        currentIndex = 0;
        Map<Direction, Integer> trafficCounts = trafficData.getAllCounts();

        if (trafficCounts.values().stream().allMatch(count -> count == 0)) {
            showAlert("All directions have zero traffic. Simulation will not start.");
            return;
        }

        AppController appController = new AppController(trafficCounts, signalStrategy);
        Map<Direction, Queue<Vehicle>> queues = appController.getSimulationManager().getTrafficQueues();

        for (Map.Entry<Direction, Queue<Vehicle>> entry : queues.entrySet()) {
            Direction direction = entry.getKey();
            Queue<Vehicle> vehicleQueue = entry.getValue();

            Queue<ImageView> imageQueue = new LinkedList<>();
            vehicleImageQueues.put(direction, imageQueue);

            int index = 0;
            for (Vehicle v : vehicleQueue) {
                String type = v.getClass().getSimpleName().toLowerCase();
                double x = getVehicleX(direction, index);
                double y = getVehicleY(direction, index);
                ImageView iv = createVehicleImage(type, x, y, direction);

                if (iv != null) {
                    imageQueue.add(iv); // Araç sıraya ekleniyor
                }

                index++;
            }

            /*// İlk aracı sahneye ekle
            if (!imageQueue.isEmpty()) {
                ImageView first = imageQueue.peek(); // sıradaki ilk aracı al
                first.setLayoutX(getVehicleX(direction, 0));
                first.setLayoutY(getVehicleY(direction, 0));
                vehicleLayer.getChildren().add(first);
            }*/

            //iLK 4 ARACI SAHNEYE EKLE
            int maxInitial = Math.min(imageQueue.size(), 4);
            for (int i = 0; i < maxInitial; i++) {
                ImageView vehicle = imageQueue.poll();
                if (vehicle != null) {
                    double x = getVehicleX(direction, i);
                    double y = getVehicleY(direction, i);
                    vehicle.setLayoutX(x);
                    vehicle.setLayoutY(y);
                    vehicleLayer.getChildren().add(vehicle);
                }
            }

        }

        while (trafficData.getVehicleCount(directionOrder.get(currentIndex)) == 0) {
            currentIndex = (currentIndex + 1) % directionOrder.size();
        }

        activateDirection(directionOrder.get(currentIndex));
    }


    @FXML
    private void onPauseClick() {
        //burayı doldur
        showAlert("Pause: Not yet implemented to halt light timers individually.");
    }

    @FXML
    private void onResumeClick() {
        //burayı doldur
        showAlert("Resume: Not yet implemented to individuals timelines.");
    }

    @FXML
    private void onResetClick() {
        trafficStatistics.reset();
        trafficData.reset();
        // tüm ışıkları kırmızıya çek
        for (Direction d : Direction.values()) {
            currentLightStates.put(d, LightState.RED);
        }
        updateLightCircles();
        clearAllTimerLabels();
        // input ekranına yönlendir
        Stage stage = (Stage) resetButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/com/traffic/view/InputView.fxml");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}




