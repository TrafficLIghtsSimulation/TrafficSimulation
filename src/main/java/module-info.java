module edu.erciyes.trafficlightcontrolsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.traffic.controller to javafx.fxml;
    exports com.traffic;
}