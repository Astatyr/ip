package lilith.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import lilith.task.Task;

/**
 * Storage class, to handle file operations.
 */
public class Storage {

    private final Path filePath;
    private String loadError = null;
    private final ArrayList<String> corruptedLines = new ArrayList<>();

    /**
     * Constructs a Storage object with the given file path.
     *
     * @param path Path to the save file.
     */
    public Storage(String path) {
        this.filePath = Paths.get(path);
    }

    /**
     * Returns any error that occurred during loadTasks(), or null if none.
     */
    public String getLoadError() {
        return loadError;
    }

    /**
     * Returns a list of any corrupted lines that were skipped during loadTasks().
     */
    public ArrayList<String> getCorruptedLines() {
        return corruptedLines;
    }

    /**
     * Creates file and directories if missing.
     *
     * @throws IOException If file cannot be created.
     */
    private void ensureFileExists() throws IOException {
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Loads tasks from the file and skips corrupted lines safely.
     * Stores any IO error in loadError for the GUI to display on startup.
     *
     * @return ArrayList of loaded tasks.
     */
    public ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        loadError = null;

        try {
            ensureFileExists();

            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    tasks.add(Task.fromFileString(line));
                } catch (Exception e) {
                    corruptedLines.add("Skipped corrupted save data: " + line);
                }
            }

        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("denied")) {
                loadError = "Permission denied: cannot read save file.";
            } else {
                loadError = "Could not load saved tasks: " + e.getMessage();
            }
        }

        return tasks;
    }

    /**
     * Overwrites the file each time a task is added or removed.
     * Throws IllegalArgumentException so Command.handle() can surface it in the GUI.
     *
     * @param tasks Current task list to save.
     * @throws IllegalArgumentException If file cannot be written.
     */
    public void saveTasks(ArrayList<Task> tasks) {
        try {
            ensureFileExists();

            List<String> lines = tasks.stream()
                    .map(Task::toFileString)
                    .toList();

            Files.write(filePath, lines, StandardCharsets.UTF_8);

        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("denied")) {
                throw new IllegalArgumentException("Permission denied: cannot write to save file.");
            } else {
                throw new IllegalArgumentException("Could not save tasks: " + e.getMessage());
            }
        }
    }
}
