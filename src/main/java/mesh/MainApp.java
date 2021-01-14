package mesh;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MainApp extends Application {
    private PrintWriter out;
    private final Label info = new Label("Disconnected. Provide a username and press Enter.");
    private final TextArea messageArea = new TextArea("Welcome to chat!\n");

    synchronized void write(final String text) {
        try {
            out.println(text);
        } catch (Exception e) {
            info.setText("Connection error!");
        }
    }

    private void status(final String text) {
        Platform.runLater(() -> info.setText(text));
    }

    private void run(final String serverAddress, final int port, final String userName) {
        try (
                Socket socket = new Socket(serverAddress, port);
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
        ) {
            Scanner in = new Scanner(is);
            out = new PrintWriter(os, true);

            while (in.hasNextLine()) {
                var line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {
                    out.println(userName);
                } else if (line.startsWith("NAMEACCEPTED")) {
                    status("Connected to " + serverAddress + ":" + port + " as " + userName + ". Write /quit to disconnect.");
                } else if (line.startsWith("MESSAGE")) {
                    messageArea.appendText(line.substring(8) + "\n");
                }
            }
        } catch (IOException e) {
            status("Connection disconnect due to I/O error: " + e.getMessage());
        }
        out = null;
        status("Disconnected from chat.");
    }

    private void connectTo(final String serverAddress, final int port, final String userName) {
        status("Connecting to " + serverAddress + ":" + port + "..");
        new Thread(() -> run(serverAddress, port, userName)).start();
    }

    @Override
    public void start(final Stage stage) {
        TextField textField = new TextField();

        BorderPane root = new BorderPane();
        root.setCenter(messageArea);
        root.setBottom(textField);
        root.setTop(info);
        messageArea.setEditable(false);
        textField.requestFocus();

        textField.setOnAction(e -> {
            if (out == null) {
                String userName = textField.getText();

                connectTo(Main.server, Main.port, userName);
            } else
                write(textField.getText());
            textField.setText("");
        });

        Scene main = new Scene(root);

        stage.setTitle("distributed-chat");
        stage.setScene(main);
        stage.show();

        PlatformImpl.addListener(new PlatformImpl.FinishListener() {
            @Override
            public void idle(boolean implicitExit) {
                System.exit(0);
            }

            @Override
            public void exitCalled() {
                System.exit(0);
            }
        });
    }
}