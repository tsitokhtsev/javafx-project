package com.tsitokhtsev;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button buttonSend1, buttonSend2;
    @FXML
    private TextField textField1, textField2;
    @FXML
    private ScrollPane scrollPane1, scrollPane2;
    @FXML
    private Label username1, username2;
    @FXML
    private VBox messages1, messages2;
    private Server server;
    private Client client;
    private static final Logger logger = LogManager.getLogger(Server.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Started application");

        try {
            server = new Server(5533);
            Thread s = new Thread(server);
            s.start();

            client = new Client(new Socket("localhost", 5533));
            Thread c = new Thread(client);
            c.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        messages1.heightProperty().addListener((observableValue, oldValue, newValue) -> scrollPane1.setVvalue((Double) newValue));
        messages2.heightProperty().addListener((observableValue, oldValue, newValue) -> scrollPane2.setVvalue((Double) newValue));

        client.receiveMessageFromServer(messages2);
        server.receiveMessageFromClient(messages1);

        buttonSend1.setOnAction(event -> {
            String message = textField1.getText();

            if (!message.isEmpty()) {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 5, 5));

                Text text = new Text(message);
                TextFlow textFlow = new TextFlow(text);
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                textFlow.setStyle("-fx-background-color: rgb(210,210,210); -fx-background-radius: 20px");

                hBox.getChildren().add(textFlow);
                messages1.getChildren().add(hBox);

                server.sendMessageToClient(message);
                textField1.clear();
            }
        });

        buttonSend2.setOnAction(event -> {
            String message = textField2.getText();

            if (!message.isEmpty()) {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 5, 5));

                Text text = new Text(message);
                TextFlow textFlow = new TextFlow(text);
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                textFlow.setStyle("-fx-background-color: rgb(210,210,210); -fx-background-radius: 20px");

                hBox.getChildren().add(textFlow);
                messages2.getChildren().add(hBox);

                client.sendMessageToServer(message);
                textField2.clear();
            }
        });
    }

    public void displayUsername(String username) {
        username1.setText(username);
        username2.setText("Buddy " + username);
    }

    public static void addLabel1(String messageFromClient, VBox vbox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 5));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        textFlow.setStyle("-fx-background-color: rgb(0,0,255); -fx-background-radius: 20px");
        text.setFill(Color.rgb(255, 255, 255));

        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> vbox.getChildren().add(hBox));
    }

    public static void addLabel2(String messageFromServer, VBox vbox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 5));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        textFlow.setStyle("-fx-background-color: rgb(0,0,255); -fx-background-radius: 20px");
        text.setFill(Color.rgb(255, 255, 255));

        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> vbox.getChildren().add(hBox));
    }
}
