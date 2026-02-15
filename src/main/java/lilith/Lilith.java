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

    /**
     * Initialize GUI components, intro message and loading tasks.
     */
    @Override
    public void start(Stage stage) {
        storage = new Storage(Config.DATA_PATH.toString());
        tasklist = storage.loadTasks();

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

        printToDialog("Hello, I'm Lilith!", "Would you like a strawberry cake?");

        if (!tasklist.isEmpty()) {
            printToDialog("Loaded " + tasklist.size() + " tasks!");
        }
    }

    /**
     * Handles user input from GUI
    */
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        printToDialog("> " + input);
        String response = Command.handle(input, tasklist, storage);
        printToDialog(response);

        userInput.clear();

        if (input.equalsIgnoreCase("bye")) {
            printToDialog("Bye-bye! I will always be here when you need me!");
            Platform.exit();
        }
    }

    /**
     * Appends text to the dialog area
    */
    private void printToDialog(String... lines) {
        for (String line : lines) {
            dialogArea.appendText(line + "\n");
        }
    }

    /**
     * Launch GUI
    */
    public static void main(String[] args) {
        launch(args);
    }
}