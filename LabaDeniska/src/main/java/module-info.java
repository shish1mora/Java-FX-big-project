module com.example.labadeniska {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.slf4j;

    opens com.example.labadeniska to javafx.fxml;
    exports com.example.labadeniska;
    exports com.example.labadeniska.controllers;
    opens com.example.labadeniska.controllers to javafx.fxml;
}