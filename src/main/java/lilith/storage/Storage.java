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

    /**
     * Constructs a Storage object with the given file path.
     *
     * @param path Path to the save file.
     */
    public Storage(String path) {
        this.filePath = Paths.get(path);
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
     *
     * @return ArrayList of loaded tasks.
     */
    public ArrayList<Task> loadTasks() {

        ArrayList<Task> tasks = new ArrayList<>();

        try {
            ensureFileExists();

            List<String> lines =
                    Files.readAllLines(filePath, StandardCharsets.UTF_8);

            for (String line : lines) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Task task = Task.fromFileString(line);
                    tasks.add(task);

                } catch (Exception e) {
                    System.out.println("Skipping corrupted line: " + line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Overwrites the file each time a task is added or removed.
     *
     * @param tasks Current task list to save.
     */
    public void saveTasks(ArrayList<Task> tasks) {

        try {
            ensureFileExists();

            List<String> lines = new ArrayList<>();

            for (Task task : tasks) {
                lines.add(task.toFileString());
            }

            Files.write(filePath, lines, StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}

