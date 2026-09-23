module com.example.guimain {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.guimain to javafx.fxml;
    exports com.example.guimain;
}