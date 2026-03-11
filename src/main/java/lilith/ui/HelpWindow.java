package lilith.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * HelpWindow displays a separate window listing all available commands.
 */
public class HelpWindow {

    private static final String[][] COMMANDS = {
        {"todo <task>", "Add a simple task"},
        {"deadline <task> /by <date>", "Add a task with a due date"},
        {"event <task> /from <date> /to <date>", "Add a task with start and end time"},
        {"update <index> [/name] [/by] [/from] [/to]", "Edit an existing task"},
        {"mark <index>", "Mark a task as done"},
        {"unmark <index>", "Mark a task as not done"},
        {"delete <index>", "Delete a task"},
        {"find <keyword>", "Search tasks by name"},
        {"list", "Show all tasks"},
        {"cheer", "You look like you need it"},
        {"bye", "Exit the app"},
        {"yes", "???"},
        {"no", "???"},
        {"/emptyall", "Clear all tasks"}
    };

    private final Stage helpStage;

    /**
     * Constructs the HelpWindow.
     * Call show() to display it.
     *
     * @param iconStream Resource stream for the window icon.
     */
    public HelpWindow(java.io.InputStream iconStream) {
        helpStage = new Stage();
        helpStage.setTitle("Lilith Help!");

        if (iconStream != null) {
            helpStage.getIcons().add(new Image(iconStream));
        }

        helpStage.setScene(buildScene());
        helpStage.setMinWidth(400);
        helpStage.setResizable(true);
    }

    /**
     * Shows the help window. If already open, brings it to the front.
     */
    public void show() {
        if (helpStage.isShowing()) {
            helpStage.toFront();
        } else {
            helpStage.show();
        }
    }

    /**
     * Builds the help window scene.
     */
    private Scene buildScene() {
        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #f0f5fa;");

        Label title = new Label("Available Commands");
        title.setStyle(
            "-fx-font-size: 16px;"
            + "-fx-font-weight: bold;"
            + "-fx-text-fill: #1a1a2e;"
        );

        Label dateNote = new Label(
            "Date formats accepted: yyyy-MM-dd HHmm, dd/MM/yyyy, yyyy-MM-dd, and more."
        );
        dateNote.setWrapText(true);
        dateNote.setStyle(
            "-fx-text-fill: #777777;"
            + "-fx-font-style: italic;"
            + "-fx-font-size: 11px;"
        );

        content.getChildren().addAll(title, dateNote);

        for (String[] cmd : COMMANDS) {
            content.getChildren().add(buildCommandRow(cmd[0], cmd[1]));
        }

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #f0f5fa; -fx-background-color: #f0f5fa;");

        return new Scene(scroll, 580, 450);
    }

    /**
     * Builds a single command row with command and description labels.
     *
     * @param command Command syntax string.
     * @param description Description of the command.
     * @return Styled HBox row.
     */
    private HBox buildCommandRow(String command, String description) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 10, 6, 10));
        row.setStyle(
            "-fx-background-color: #ffffff;"
            + "-fx-background-radius: 8;"
            + "-fx-border-color: #97bbdb;"
            + "-fx-border-radius: 8;"
            + "-fx-border-width: 1;"
        );

        Label cmdLabel = new Label(command);
        cmdLabel.setMinWidth(280);
        cmdLabel.setStyle(
            "-fx-font-family: monospace;"
            + "-fx-font-weight: bold;"
            + "-fx-text-fill: #2a4a6b;"
        );

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #555555;");

        row.getChildren().addAll(cmdLabel, descLabel);
        return row;
    }
}
