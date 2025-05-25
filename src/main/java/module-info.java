module com.example.traffic {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.traffic to javafx.fxml;
    exports com.example.traffic;
}