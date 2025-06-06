package com.traffic.controller;

import com.traffic.model.*;
import com.traffic.model.enums.Direction;
import com.traffic.model.enums.LightState;
import com.traffic.model.interfaces.ISignalCalculationStrategy;
import com.traffic.util.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
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
public class SimulationViewController {

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
    @FXML private Label totalVehicleLabel;

    @FXML private Button startButton, pauseButton, resumeButton, resetButton;

    @FXML private TableView<TrafficStatisticsEntry> statisticsTable;

    @FXML private TableColumn<TrafficStatisticsEntry, String> directionColumn;
    @FXML private TableColumn<TrafficStatisticsEntry, Number> passingColumn;
    @FXML private TableColumn<TrafficStatisticsEntry, Number> remainingColumn;

    @FXML private Pane vehicleLayer;

    private final Map<Direction, LightState> currentLightStates = new EnumMap<>(Direction.class);
    private final TrafficData trafficData = TrafficData.getInstance();
    private final TrafficStatistics trafficStatistics = TrafficStatistics.getInstance();
    private List<Direction> directionOrder;
    private int currentIndex = 0;

    private final ISignalCalculationStrategy signalStrategy = new ProportionalCalculationStrategy();

    // Her yön için sahnedeki araçların ImageView kuyruğu
    private final Map<Direction, Queue<ImageView>> vehicleImageQueues = new EnumMap<>(Direction.class);

    //pause ve resume işlemlerinde o an aktif olan nesneleri tutar
    private List<Timeline> activeTimelines = new ArrayList<>();
    private List<PathTransition> activeTransitions = new ArrayList<>();
    private boolean isPaused = false;
    private boolean isRunning = false;

    public void initialize() {
        updateTotalVehicleLabel();
        setupTable();
        setupLightStates();
        setStatesButtons();
    }

    private void updateTotalVehicleLabel() {
        int north = trafficData.getVehicleCount(Direction.NORTH);
        int south = trafficData.getVehicleCount(Direction.SOUTH);
        int east = trafficData.getVehicleCount(Direction.EAST);
        int west = trafficData.getVehicleCount(Direction.WEST);

        int totalVehicles = north + south + east + west;

        totalVehicleLabel.setText(
                "North: " + north + "   |   " +
                        "South: " + south + "   |   " +
                        "East: " + east + "   |   " +
                        "West: " + west + "   ||   " +
                        "Total: " + totalVehicles
        );
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
        activeTimelines.add(countdown);
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
        startCountdownTimer(direction, greenDuration, Paint.valueOf("#007200"));// Yeşil renkte geri sayım başlasın
        scheduleVehicleMovements(direction, greenDuration);


        // İstatistik güncelleme
        int totalVehicles = trafficData.getVehicleCount(direction);
        int estimatedPassing = Math.min(greenDuration, vehicleImageQueues.getOrDefault(direction, new LinkedList<>()).size());
        int remaining = totalVehicles - estimatedPassing;
        trafficData.setVehicleCount(direction, remaining);
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

        //aktif timelineları listeye eklerim
        activeTimelines.add(yellowTimeline);
        activeTimelines.add(redTimeline);
        activeTimelines.add(nextDirectionTimeline);
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
            imageView.setFitWidth(28);
            imageView.setFitHeight(30);
            imageView.setPreserveRatio(true);

            // Mutlaka translate değerlerini sıfırla (hareket görünmeme hatası engellenir)
            imageView.setTranslateX(0);
            imageView.setTranslateY(0);

            imageView.setLayoutX(x);
            imageView.setLayoutY(y);

            double angle = switch (direction) {
                case NORTH -> 180;
                case SOUTH -> 0;
                case EAST  -> 270;
                case WEST  -> 90;
            };
            imageView.setRotate(angle);

            // Debug çıktısı
            System.out.println("Araç oluşturuldu: " + direction);
            System.out.println("Başlangıç pozisyonu: layoutX=" + imageView.getLayoutX() + ", layoutY=" + imageView.getLayoutY());
            System.out.println("Pane boyutu: width=" + vehicleLayer.getWidth() + ", height=" + vehicleLayer.getHeight());

            return imageView;
        } catch (Exception e) {
            System.out.println("Image not loaded: " + imagePath);
            e.printStackTrace();
            return null;
        }
    }


    private double getVehicleX(Direction direction, int index) {
        //ilk aracın konumlandırılması için gerekli koordinatlar
        if (index == 0) {
            return switch (direction) {
                case NORTH -> 310;
                case SOUTH -> 380;
                case EAST  -> 460; //447
                case WEST -> 230;
            };
        }
        return switch (direction) {
            case NORTH -> 310;
            case SOUTH -> 380;
            case EAST  -> 700;
            case WEST  -> 0;
        };
    }

    private double getVehicleY(Direction direction, int index) {
        //ilk aracın konumlandırılması için gerekli koordinatlar
        if (index == 0) {
            return switch (direction) {
                case NORTH -> 10; //154
                case SOUTH -> 372; //395
                case EAST  -> 238;
                case WEST -> 300;
            };
        }
        return switch (direction) {
            case NORTH -> 10;
            case SOUTH -> 520;
            case EAST  -> 238;
            case WEST  -> 300;
        };
    }



    private int getCurrentVehicleCount(Direction direction) {
        int count = 0;
        for (Node node : vehicleLayer.getChildren()) {
            if (node instanceof ImageView) {
                ImageView iv = (ImageView) node;
                // Her aracın fx:id'si ya da kullanıcıData'sı ile yönü tutuluyorsa buraya kontrol ekleyebilirsin.
                // Ancak şu an queue bazlı gidiyoruz, o yüzden her yön için 4 araç limiti olduğu varsayımı yeterli:
                count++;
            }
        }
        return count;
    }

    private void addNextVehicleIfAvailable(Direction direction) {
        Queue<ImageView> imageQueue = vehicleImageQueues.get(direction);
        int currentCount = getCurrentVehicleCount(direction); // sahnedeki araç sayısı

        if (imageQueue != null && !imageQueue.isEmpty() && currentCount < 4) {
            ImageView nextVehicle = imageQueue.peek(); // sıradaki aracı gör
            if (nextVehicle != null) {
                double x = getVehicleX(direction, currentCount);
                double y = getVehicleY(direction, currentCount);
                nextVehicle.setLayoutX(x);
                nextVehicle.setLayoutY(y);
                vehicleLayer.getChildren().add(nextVehicle);
            }
        }
    }


    private void scheduleVehicleMovements(Direction direction, int greenDuration) {
        Queue<ImageView> queue = vehicleImageQueues.get(direction);
        if (queue == null || queue.isEmpty()) {
            return;
        }

        Timeline vehicleTimeline = new Timeline();
        int count = Math.min(greenDuration, queue.size());

        for (int i = 0; i < count; i++) {
            KeyFrame frame = new KeyFrame(Duration.seconds(i), e -> {
                animateVehicle(direction);
            });
            vehicleTimeline.getKeyFrames().add(frame);
        }

        vehicleTimeline.play();
        activeTimelines.add(vehicleTimeline);
    }

private void animateVehicle(Direction direction) {
        if (currentLightStates.get(direction) != LightState.GREEN) {
            System.out.println("Kırmızı/Sarı ışıkta hareket engellendi: " + direction);
            return;
        }

        System.out.println("animateVehicle çağrıldı: " + direction);

        Queue<ImageView> imageQueue = vehicleImageQueues.get(direction);
        if (imageQueue == null || imageQueue.isEmpty()) {
            System.out.println("Kuyruk boş veya null: " + direction);
            return;
        }

        ImageView vehicle = imageQueue.poll();
        if (vehicle == null) {
            System.out.println("Araç null geldi.");
            return;
        }

        // Eğer araç sahnede değilse sahneye ekle
        if (!vehicleLayer.getChildren().contains(vehicle)) {
            vehicleLayer.getChildren().add(vehicle);
        }

        double localStartX = 0;
        double localStartY = 0;
        double localEndX = 0;
        double localEndY = 0;

        switch (direction) {
            case NORTH -> localEndY = 524;
            case SOUTH -> localEndY = -524;
            case EAST  -> localEndX = -700;
            case WEST  -> localEndX = 700;
        }

        System.out.println("Hareket: " + direction + " start=(" + localStartX + ", " + localStartY + ") → end=(" + localEndX + ", " + localEndY + ")");


        Path path = new Path(
                new MoveTo(localStartX, localStartY),
                new LineTo(localEndX, localEndY)
        );

        PathTransition transition = new PathTransition(Duration.seconds(3), path, vehicle);
        transition.setOrientation(PathTransition.OrientationType.NONE);
        activeTransitions.add(transition);
        transition.setOnFinished(e -> {
            vehicleLayer.getChildren().remove(vehicle);
            addNextVehicleIfAvailable(direction);
            activeTransitions.remove(transition);
        });
        transition.play();
    }


    @FXML
    private void onStartSimulationClick() {
        isRunning = true;
        setStatesButtons();

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

            // İlk aracı sahneye ekle
            if (!imageQueue.isEmpty()) {
                ImageView first = imageQueue.peek(); // sıradaki ilk aracı al
                first.setLayoutX(getVehicleX(direction, 0));
                first.setLayoutY(getVehicleY(direction, 0));
                vehicleLayer.getChildren().add(first);
            }

            /*
            //Kuyruktaki ilk 4 aracı sahneye ekleme
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
            }*/
        }

        while (trafficData.getVehicleCount(directionOrder.get(currentIndex)) == 0) {
            currentIndex = (currentIndex + 1) % directionOrder.size();
        }
        activateDirection(directionOrder.get(currentIndex));
    }



    @FXML
    private void onPauseClick() {
        if (isPaused) return;
        isPaused = true;
        setStatesButtons();

        // Tüm aktif timeline'ları duraklat
        for (Timeline timeline : activeTimelines) {
            if (timeline != null) {
                timeline.pause();
            }
        }

        // Tüm araç animasyonlarını duraklat
        for (PathTransition transition : activeTransitions) {
            if (transition != null) {
                transition.pause();
            }
        }

        System.out.println("Simülasyon duraklatıldı");
    }

    @FXML
    private void onResumeClick() {
        if(!isPaused) return;
        isPaused = false;
        setStatesButtons();

        for (Timeline timeline : activeTimelines) {
            if (timeline != null) {
                timeline.play();
            }
        }

        // Tüm araç animasyonlarını devam ettir
        for (PathTransition transition : activeTransitions) {
            if (transition != null) {
                transition.play();
            }
        }

        System.out.println("Simülasyon devam ettirildi");
    }


    @FXML
    private void onResetClick() {
        //durdurma ve temizleme işlemleri
        for(Timeline timeline : activeTimelines) {
            if (timeline != null) {
                timeline.stop();
            }
        }
        activeTimelines.clear();

        for(PathTransition transition : activeTransitions) {
            if (transition != null) {
                transition.stop();
            }
        }
        activeTimelines.clear();

        //araçları kaldırma
        vehicleLayer.getChildren().clear();
        vehicleImageQueues.clear();
        isRunning = false;
        setStatesButtons();
        //lambalar kırmızı
        for(Direction direction : Direction.values()) {
            currentLightStates.put(direction,LightState.RED);
        }
        updateLightCircles();

        clearAllTimerLabels();
        trafficStatistics.reset();
        trafficData.reset();
        Stage stage =(Stage) resetButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/com/traffic/view/InputView.fxml");
    }



    private void setStatesButtons(){
        startButton.setDisable(isRunning);
        pauseButton.setDisable(!isRunning||isPaused);
        resumeButton.setDisable(!isRunning||!isPaused);
        resetButton.setDisable(false);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}




