package lilith;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lilith.command.Command;
import lilith.config.Config;
import lilith.storage.Storage;
import lilith.task.Task;

/**
 * Lilith GUI application.
 * Uses Command as backend to handle both CLI and GUI commands.
 */
public class Lilith extends Application {

    private Storage storage;
    private ArrayList<Task> tasklist;

    private TextArea dialogArea;
    private TextField userInput;
    private Button sendButton;

    @Override
    public void start(Stage stage) {
        storage = new Storage(Config.DATA_PATH.toString());
        tasklist = storage.loadTasks();

        // GUI components
        dialogArea = new TextArea();
        dialogArea.setEditable(false);
        dialogArea.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(dialogArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        userInput = new TextField();
        sendButton = new Button("Send");

        sendButton.setOnAction(e -> handleUserInput());
        userInput.setOnAction(e -> handleUserInput());

        VBox layout = new VBox(scrollPane, userInput, sendButton);
        Scene scene = new Scene(layout, 600, 400);

        stage.setScene(scene);
        stage.setTitle("Lilith Chatbot GUI");
        stage.show();

        // Show intro message
        printToDialog("Hello, I'm Lilith!");
        printToDialog("Would you like a strawberry cake?");

        // Show loaded tasks if any
        if (!tasklist.isEmpty()) {
            printToDialog("Loaded " + tasklist.size() + " tasks!");
        }
    }

    /** Handles user input from GUI */
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        printToDialog("> " + input); // Echo input
        String response = Command.handle(input, tasklist, storage);
        printToDialog(response);

        userInput.clear();

        if (input.equalsIgnoreCase("bye")) {
            Platform.exit();
        }
    }

    /** Appends text to the dialog area */
    private void printToDialog(String text) {
        dialogArea.appendText(text + "\n");
    }

    /** Launch GUI */
    public static void main(String[] args) {
        launch(args);
    }
}


