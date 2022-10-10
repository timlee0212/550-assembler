module com.ece550 {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;
    opens com.ece550 to javafx.fxml;
    exports com.ece550;
}