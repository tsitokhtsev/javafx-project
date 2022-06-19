package com.tsitokhtsev;

import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final int port;
    private ServerSocket socket;
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private static final Logger logger = LogManager.getLogger(Server.class);

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new ServerSocket(port);
            clientSocket = socket.accept();

            logger.info("Got new client: " +
                clientSocket.getInetAddress().getHostAddress() +
                ":" +
                clientSocket.getPort());

            var inputStream = clientSocket.getInputStream();
            var outputStream = clientSocket.getOutputStream();
            var inputStreamReader = new InputStreamReader(inputStream);
            var outputStreamWriter = new OutputStreamWriter(outputStream);
            this.bufferedReader = new BufferedReader(inputStreamReader);
            this.bufferedWriter = new BufferedWriter(outputStreamWriter);
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessageToClient(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Error sending message");
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receiveMessageFromClient(VBox vBox) {
        new Thread(() -> {
            while (clientSocket.isConnected()) {
                try {
                    String message = bufferedReader.readLine();
                    MainController.addLabel1(message, vBox);
                } catch (IOException e) {
                    System.out.println("Error receiving message");
                    e.printStackTrace();
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    public void closeEverything(ServerSocket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
