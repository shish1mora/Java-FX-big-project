module com.example.pacient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pacient to javafx.fxml;
    exports com.example.pacient;
}