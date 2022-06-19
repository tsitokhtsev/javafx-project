package com.tsitokhtsev;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WelcomeController {
    @FXML
    private TextField usernameField;
    private static final Logger logger = LogManager.getLogger(App.class);

    @FXML
    private void login(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("/views/main.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        String username = usernameField.getText();
        saveUser(username);

        MainController mainController = fxmlLoader.getController();
        mainController.displayUsername(username);

        stage.setScene(mainScene);
        stage.show();
    }

    public void saveUser(String username) throws SQLException {
        Connection connection = Database.getConnection();

        if (!connection.isClosed()) {
            logger.info("Connected to database");

            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO USERS(NAME) VALUES(?)"
            )) {
                preparedStatement.setObject(1, username);
                preparedStatement.executeUpdate();
                logger.info("Inserted " + username + " into database");
            }
        }
    }
}
