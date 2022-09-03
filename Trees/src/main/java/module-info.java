module com.enteractive.trees {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens com.enteractive.trees to javafx.fxml;
    exports com.enteractive.trees;
}