package com.tsitokhtsev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {
    private static final Logger logger = LogManager.getLogger(App.class);

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Parent welcomeParent = new FXMLLoader(App.class.getResource("/views/welcome.fxml")).load();
        Scene scene = new Scene(welcomeParent);
        stage.setTitle("Chat for Introverts");
        stage.setScene(scene);
        stage.show();

        logger.info("Application started");
    }
}
