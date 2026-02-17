package lilith.command;

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;

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

        String trimmedInput = userInput.trim();
        String userInputLower = trimmedInput.toLowerCase();

        try {

            /**
             * Exact commands
             */

            switch (userInputLower) {

            case Config.CMD_CHEER:
                return openCheerLink();

            case Config.CMD_YES:
                return "Okay, but convince the oven it's not a time machine!\n";

            case Config.CMD_NO:
                return "Lilith is sad...\n";

            case Config.CMD_LIST:
                return listTasks(taskList);

            case Config.CMD_EMPTY_ALL:
                taskList.clear();
                storage.saveTasks(taskList);
                return "All tasks have been cleared!\n";

            case Config.CMD_BYE:
                return "Bye-bye! I will always be here.\n";

            default:
                break;
            }

            /**
             * Commands that starts with a keyword 
             */

            if (userInputLower.startsWith(Config.CMD_FIND)) {
                return handleFind(trimmedInput, taskList);
            }

            if (userInputLower.startsWith(Config.CMD_TODO)) {
                return handleTodo(trimmedInput, taskList, storage);
            }

            if (userInputLower.startsWith(Config.CMD_DEADLINE)) {
                return handleDeadline(trimmedInput, taskList, storage);
            }

            if (userInputLower.startsWith(Config.CMD_EVENT)) {
                return handleEvent(trimmedInput, taskList, storage);
            }

            if (userInputLower.startsWith(Config.CMD_MARK)) {
                return handleMark(trimmedInput, taskList, storage);
            }

            if (userInputLower.startsWith(Config.CMD_UNMARK)) {
                return handleUnmark(trimmedInput, taskList, storage);
            }

            if (userInputLower.startsWith(Config.CMD_DELETE)
                    || userInputLower.startsWith(Config.CMD_DEL)
                    || userInputLower.startsWith(Config.CMD_REMOVE)) {
                return handleDelete(trimmedInput, taskList, storage);
            }

            return "Lilith cannot find the task type...\n";

        } catch (IndexOutOfBoundsException e) {
            return "That task does not exist!\n";

        } catch (NumberFormatException e) {
            return "Please provide a valid task number.\n";

        } catch (IllegalArgumentException e) {
            return e.getMessage() + "\n";
        }
    }

    /**
     * Opens the Cheer link in the default browser.
     */
    private static String openCheerLink() {

        if (!Config.CHEER_LINK.startsWith("https://www.youtube.com/")) {
            return "Invalid URL. Cannot open.\n";
        }

        if (!Desktop.isDesktopSupported()) {
            return "Desktop API not supported on this system.\n";
        }

        try {
            Desktop.getDesktop().browse(new URI(Config.CHEER_LINK));
            return "Cheering GO! Link opened.\n";

        } catch (Exception e) {
            return "Failed to open site.\n";
        }
    }

    /**
     * Lists all tasks in the task list.
     */
    private static String listTasks(ArrayList<Task> taskList) {

        if (taskList.isEmpty()) {
            return "You're free!\n";
        }

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < taskList.size(); i++) {
            output.append(i + 1).append(". ").append(taskList.get(i)).append("\n");
        }

        return output.toString();
    }

    /**
     * Adds a task to the list and saves it.
     */
    private static String addTask(Task task, ArrayList<Task> taskList, Storage storage) {

        taskList.add(task);
        storage.saveTasks(taskList);

        return "Got it. I've added this task:\n" + task + "\n";
    }

    /**
     * Extracts the task index from a command.
     */
    private static int parseIndex(String input, String command) {

        return Integer.parseInt(input.substring(command.length()).trim()) - 1;
    }

    /**
     * Handles the find command.
     */
    private static String handleFind(String trimmedInput, ArrayList<Task> taskList) {

        String keyword = trimmedInput.substring(Config.CMD_FIND.length()).trim();

        if (keyword.isEmpty()) {
            return "Include which task you are looking for!\n";
        }

        StringBuilder output = new StringBuilder();
        int matchCount = 0;

        for (Task task : taskList) {
            if (task.getTaskname().toLowerCase().contains(keyword.toLowerCase())) {
                matchCount++;
                output.append(matchCount).append(". ").append(task).append("\n");
            }
        }

        if (matchCount == 0) {
            return "No matching tasks found for \"" + keyword + "\".\n";
        }

        return output.toString();
    }

    /**
     * Handles adding a todo task.
     */
    private static String handleTodo(String trimmedInput, ArrayList<Task> taskList, Storage storage) {

        String taskName = trimmedInput.substring(Config.CMD_TODO.length()).trim();
        Task task = new Task(taskName, null, null);
        task.setTask(Task.TaskType.ToDos);

        return addTask(task, taskList, storage);
    }

    /**
     * Handles adding a deadline task.
     */
    private static String handleDeadline(String trimmedInput, ArrayList<Task> taskList, Storage storage) {

        String deadlineInput = trimmedInput.substring(Config.CMD_DEADLINE.length()).trim();
        String[] parts = Parser.parseDeadlineInput(deadlineInput);

        Task task = new Task(parts[0], null, parts[1]);
        task.setTask(Task.TaskType.Deadline);

        return addTask(task, taskList, storage);
    }

    /**
     * Handles adding an event task.
     */
    private static String handleEvent(String trimmedInput, ArrayList<Task> taskList, Storage storage) {

        String eventInput = trimmedInput.substring(Config.CMD_EVENT.length()).trim();
        String[] parts = Parser.parseEventInput(eventInput);

        Task task = new Task(parts[0], parts[1], parts[2]);
        task.setTask(Task.TaskType.Events);

        return addTask(task, taskList, storage);
    }

    /**
     * Handles marking a task as done.
     */
    private static String handleMark(String trimmedInput, ArrayList<Task> taskList, Storage storage) {

        int taskIndex = parseIndex(trimmedInput, Config.CMD_MARK);

        taskList.get(taskIndex).mark();
        storage.saveTasks(taskList);

        return "Nicely done! Good job!\n" + taskList.get(taskIndex) + "\n";
    }

    /**
     * Handles unmarking a task as not done.
     */
    private static String handleUnmark(String trimmedInput, ArrayList<Task> taskList, Storage storage) {

        int taskIndex = parseIndex(trimmedInput, Config.CMD_UNMARK);

        taskList.get(taskIndex).unmark();
        storage.saveTasks(taskList);

        return "Make sure to finish it soon, ok?\n" + taskList.get(taskIndex) + "\n";
    }

    /**
     * Handles deleting a task.
     */
    private static String handleDelete(String trimmedInput, ArrayList<Task> taskList, Storage storage) {

        String[] words = trimmedInput.split("\\s+");
        int taskIndex = Integer.parseInt(words[1]) - 1;

        Task removedTask = taskList.remove(taskIndex);
        storage.saveTasks(taskList);

        return "Ta-da! I have removed the task:\n" + removedTask + "\n";
    }
}





