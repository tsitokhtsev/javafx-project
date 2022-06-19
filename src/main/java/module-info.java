module com.tsitokhtsev {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires com.h2database;
    requires java.sql;
    requires java.naming;

    opens com.tsitokhtsev to javafx.fxml;
    exports com.tsitokhtsev;
}
