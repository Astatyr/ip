import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    private final Path filePath;

    public Storage(String path) {
        this.filePath = Paths.get(path);
    }

    /**
     * Ensures that the folder and file exist.
     * Creates them if missing.
     */
    private void ensureFileExists() throws IOException {

        // Create ./data folder if missing
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        // Create file if missing
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Loads tasks from the file.
     * Skips corrupted lines safely.
     */
    public ArrayList<Task> loadTasks() {

        ArrayList<Task> tasks = new ArrayList<>();

        try {
            ensureFileExists();

            // Read all lines at once
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

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
     * Saves tasks into the file using Files.write().
     * Overwrites the file each time.
     */
    public void saveTasks(ArrayList<Task> tasks) {

        try {
            ensureFileExists();

            List<String> lines = new ArrayList<>();

            // Convert each task into a file line
            for (Task task : tasks) {
                lines.add(task.toFileString());
            }

            // Write all lines into file (overwrite)
            Files.write(filePath, lines, StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}
