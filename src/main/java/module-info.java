module com.pimms.createassessment {
    requires javafx.controls;
    requires javafx.fxml;
    requires kernel;
    requires layout;
    requires io;
    requires json.simple;
    requires org.controlsfx.controls;

    opens com.pimms.createassessment to javafx.fxml;
    exports com.pimms.createassessment;
    exports com.pimms.createassessment.models;
}