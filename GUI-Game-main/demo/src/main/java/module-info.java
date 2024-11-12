module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson; 
    requires javafx.media;

    opens com.example to javafx.fxml, com.google.gson; // Open the package to Gson
    exports com.example;
}
