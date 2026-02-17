package lilith.command;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lilith.config.Config;
import lilith.parser.Parser;
import lilith.storage.Storage;
import lilith.task.Task;

/**
 * Command class handles all user commands for CLI and GUI.
 */
public class Command {

    /**
     * Handles a user input command.
     *
     * @param userInput Original user input string
     * @param taskList  List of tasks
     * @param storage   Storage handler
     * @return Result of the command as a String
     */
    public static String handle(String userInput, ArrayList<Task> taskList, Storage storage) {
        assert userInput != null : "Command handler received null input";
        assert taskList != null : "Task list should not be null";
        assert storage != null : "Storage should not be null";
            
        StringBuilder output = new StringBuilder();

        try {
            String trimmedInput = userInput.trim();
            String userInputLower = trimmedInput.toLowerCase();

            if (userInputLower.equals(Config.CMD_CHEER)) {
                openCheerLink();
                output.append("Cheering GO! Link opened.\n");

            } else if (userInputLower.equals(Config.CMD_YES)) {
                output.append("Okay, but convince the oven it's not a time machine!\n");

            } else if (userInputLower.equals(Config.CMD_NO)) {
                output.append("Lilith is sad...\n");

            } else if (userInputLower.equals(Config.CMD_LIST)) {
                if (taskList.isEmpty()) {
                    output.append("You're free!\n");
                } else {
                    return IntStream.range(0, taskList.size())
                            .mapToObj(i -> (i + 1) + ". " + taskList.get(i))
                            .collect(Collectors.joining("\n"))
                            + "\n";

                }

            } else if (userInputLower.startsWith(Config.CMD_FIND)) {

                String keyword = trimmedInput.substring(Config.CMD_FIND.length()).trim();

                if (keyword.isEmpty()) {
                    output.append("Include which task you are looking for!\n");
                    return output.toString();
                }

                String keywordLower = keyword.toLowerCase();

                List<Task> matches = taskList.stream()
                        .filter(task -> task.getTaskname()
                                .toLowerCase()
                                .contains(keywordLower))
                        .toList();

                if (matches.isEmpty()) {
                    output.append("No matching tasks found for \"")
                            .append(keyword)
                            .append("\".\n");
                } else {
                    for (int i = 0; i < matches.size(); i++) {
                        output.append(i + 1)
                                .append(". ")
                                .append(matches.get(i))
                                .append("\n");
                    }
                }

            } else if (userInputLower.startsWith(Config.CMD_TODO)) {
                String taskName = trimmedInput.substring(Config.CMD_TODO.length()).trim();
                Task task = new Task(taskName, null, null);
                task.setTask(Task.TaskType.ToDos);
                taskList.add(task);
                storage.saveTasks(taskList);
                output.append("Got it. I've added this task:\n").append(task).append("\n");

            } else if (userInputLower.startsWith(Config.CMD_DEADLINE)) {
                String deadlineInput = trimmedInput.substring(Config.CMD_DEADLINE.length()).trim();
                String[] parts = Parser.parseDeadlineInput(deadlineInput);
                assert parts.length == 2 : "Deadline parser contract broken";
                Task task = new Task(parts[0], null, parts[1]);
                task.setTask(Task.TaskType.Deadline);
                taskList.add(task);
                storage.saveTasks(taskList);
                output.append("Got it. I've added this task:\n").append(task).append("\n");

            } else if (userInputLower.startsWith(Config.CMD_EVENT)) {
                String eventInput = trimmedInput.substring(Config.CMD_EVENT.length()).trim();
                String[] parts = Parser.parseEventInput(eventInput);
                Task task = new Task(parts[0], parts[1], parts[2]);
                assert parts.length == 3 : "Event parser contract broken";
                task.setTask(Task.TaskType.Events);
                taskList.add(task);
                storage.saveTasks(taskList);
                output.append("Got it. I've added this task:\n").append(task).append("\n");

            } else if (userInputLower.startsWith(Config.CMD_MARK)) {
                int taskIndex = Integer.parseInt(trimmedInput.substring(Config.CMD_MARK.length()).trim()) - 1;
                taskList.get(taskIndex).mark();
                storage.saveTasks(taskList);
                output.append("Nicely done! Good job!\n").append(taskList.get(taskIndex)).append("\n");

            } else if (userInputLower.startsWith(Config.CMD_UNMARK)) {
                int taskIndex = Integer.parseInt(trimmedInput.substring(Config.CMD_UNMARK.length()).trim()) - 1;
                taskList.get(taskIndex).unmark();
                storage.saveTasks(taskList);
                output.append("Make sure to finish it soon, ok?\n").append(taskList.get(taskIndex)).append("\n");

            } else if (userInputLower.startsWith(Config.CMD_DELETE)
                    || userInputLower.startsWith(Config.CMD_DEL)
                    || userInputLower.startsWith(Config.CMD_REMOVE)) {
                int taskIndex = Integer.parseInt(trimmedInput.replaceAll("^(delete|del|remove)\\s+", "")) - 1;
                Task removedTask = taskList.remove(taskIndex);
                storage.saveTasks(taskList);
                output.append("Ta-da! I have removed the task:\n").append(removedTask).append("\n");

            } else if (userInputLower.equals(Config.CMD_EMPTY_ALL)) {
                taskList.clear();
                storage.saveTasks(taskList);
                output.append("All tasks have been cleared!\n");

            } else if (userInputLower.equals(Config.CMD_BYE)) {
                output.append("Bye-bye! I will always be here.\n");

            } else {
                output.append("Lilith cannot find the task type...\n");
            }

        } catch (IndexOutOfBoundsException e) {
            output.append("That task does not exist!\n");
        }

        return output.toString();
    }

    /**
     * Opens the Cheer link in the default browser.
     */
    private static void openCheerLink() {
        if (!Config.CHEER_LINK.startsWith("https://www.youtube.com/")) {
            System.out.println("Invalid URL. Cannot open.");
            return;
        }

        if (!Desktop.isDesktopSupported()) {
            System.out.println("Desktop API not supported on this system.");
            return;
        }

        try {
            URI uri = new URI(Config.CHEER_LINK);
            Desktop.getDesktop().browse(uri);
            System.out.println("Cheering operation, GO!");

        } catch (URISyntaxException e) {
            System.out.println("Invalid URI syntax: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("Failed to open browser: " + e.getMessage());

        } catch (SecurityException e) {
            System.out.println("Permission denied to open browser: " + e.getMessage());
        }
    }
}



