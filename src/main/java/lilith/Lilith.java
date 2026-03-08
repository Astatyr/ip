package lilith;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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

    private VBox chatBox;
    private ScrollPane scrollPane;
    private TextField userInput;
    private Button sendButton;
    private boolean lastMessageWasUser = true;

    /**
     * Initialize GUI components, intro message and loading tasks.
     * Uses VBox for chat area and HBox for input row.
     * Styles messages with CSS for better appearance.
     * Auto-scrolls to the latest message when new content is added.
     * Handles user input and bot responses, including error styling.
     * Shows profile picture and name header for bot messages only at the start of a new bot series.
     * Exits application gracefully on "bye" command.
     */
    @Override
    public void start(Stage stage) {
        storage = new Storage(Config.DATA_PATH);
        tasklist = storage.loadTasks();

        chatBox = new VBox(8);
        chatBox.setPadding(new Insets(10));
        chatBox.setStyle("-fx-background-color: #bbddff;");

        scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #bbddff; -fx-background-color: #bbddff;");
        scrollPane.vvalueProperty().bind(chatBox.heightProperty());

        userInput = new TextField();
        userInput.setPromptText("Type a command...");
        userInput.setStyle(
            "-fx-background-color: #ffffff;"
            + "-fx-text-fill: #1a1a2e;"
            + "-fx-border-color: #97bbdb;"
            + "-fx-border-radius: 12;"
            + "-fx-background-radius: 12;"
            + "-fx-padding: 8 12;"
        );
        HBox.setHgrow(userInput, Priority.ALWAYS);

        sendButton = new Button("Send");
        sendButton.setStyle(
            "-fx-background-color: #2a4a6b;"
            + "-fx-text-fill: #e8e8f0;"
            + "-fx-background-radius: 12;"
            + "-fx-padding: 8 16;"
            + "-fx-cursor: hand;"
        );

        sendButton.setOnAction(e -> handleUserInput());
        userInput.setOnAction(e -> handleUserInput());

        HBox inputRow = new HBox(8, userInput, sendButton);
        inputRow.setPadding(new Insets(8, 10, 10, 10));
        inputRow.setAlignment(Pos.CENTER);
        inputRow.setStyle("-fx-background: #f0f5fa; -fx-background-color: #f0f5fa;");

        VBox layout = new VBox(scrollPane, inputRow);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        Scene scene = new Scene(layout, 600, 500);

        stage.setScene(scene);
        stage.setTitle("Lilith");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/lilith/icon.png")));
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.show();

        addBotMessage("Hello, I'm Lilith!");
        addBotMessage("Would you like a strawberry cake?");

        if (!tasklist.isEmpty()) {
            addBotMessage("I've found " + tasklist.size() + " task(s) saved!");
        }
    }

    /**
     * Handles user input from GUI.
     */
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        addUserMessage(input);
        String response = Command.handle(input, tasklist, storage);
        addBotMessageOrError(response, isErrorResponse(response));

        userInput.clear();

        if (input.equalsIgnoreCase("bye")) {
            Platform.exit();
        }
    }

    /**
     * Checks if a bot response is an error message.
     * Used to style errors differently.
     */
    private boolean isErrorResponse(String response) {
        String lower = response.toLowerCase();
        return lower.contains("cannot")
            || lower.contains("does not exist")
            || lower.contains("please provide")
            || lower.contains("missing")
            || lower.contains("use:")
            || lower.contains("invalid");
    }

    /**
     * Adds a user message bubble — right-aligned, purple tint.
     */
    private void addUserMessage(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(400);
        label.setStyle(
        "-fx-background-color: #ffffff;"
        + "-fx-text-fill: #1a1a2e;"
        + "-fx-background-radius: 12 12 2 12;"
            + "-fx-padding: 8 12;"
        );

        HBox row = new HBox(label);
        row.setAlignment(Pos.CENTER_RIGHT);
        chatBox.getChildren().add(row);
        lastMessageWasUser = true;
    }

    /**
     * Adds a bot message bubble — left-aligned, dark gray or red for errors.
     */
    private void addBotMessage(String text) {
        addBotMessageOrError(text, false);
    }

    /**
     * Adds a bot message bubble with optional error styling.
     * Load and show circular profile picture and name header only if the last message was from the user.
     */
    private void addBotMessageOrError(String text, boolean isError) {
        if (lastMessageWasUser) {
            javafx.scene.image.Image pfpImage = new javafx.scene.image.Image(
                getClass().getResourceAsStream("/lilith/pfp.png")
            );
            javafx.scene.image.ImageView pfpView = new javafx.scene.image.ImageView(pfpImage);
            pfpView.setFitWidth(32);
            pfpView.setFitHeight(32);

            javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(16, 16, 16);
            pfpView.setClip(clip);

            Label nameLabel = new Label("Lilith");
            nameLabel.setStyle(
                "-fx-text-fill: #c02a2a;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 4 0 0 6;"
                + "-fx-effect: dropshadow(three-pass-box, #2a4a6b, 1, 1, 0, 0);"
            );

            HBox nameRow = new HBox(5, pfpView, nameLabel); // 5px gap between circle and name
            nameRow.setAlignment(Pos.CENTER_LEFT);
            nameRow.setPadding(new Insets(0, 0, 0, 12));
            chatBox.getChildren().add(nameRow);
        }

        String bubbleColor = isError ? "#982121" : "#2a4a6b";
        String borderColor = isError ? "#720000" : "#c8dff0";

        Label label = new Label(text.trim());
        label.setWrapText(true);
        label.setMaxWidth(400);
        label.setStyle(
            "-fx-background-color: " + bubbleColor + ";"
            + "-fx-text-fill: #ffffff;"
            + "-fx-border-color: " + borderColor + ";"
            + "-fx-border-width: 1;"
            + "-fx-background-radius: 12 12 12 2;"
            + "-fx-border-radius: 12 12 12 2;"
            + "-fx-padding: 8 12;"
        );

        HBox row = new HBox(label);
        row.setAlignment(Pos.CENTER_LEFT);
        chatBox.getChildren().add(row);

        lastMessageWasUser = false;
    }

    /**
     * Launch GUI.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
