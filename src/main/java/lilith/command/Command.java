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
     * @param input    User input string
     * @param tasklist List of tasks
     * @param storage  Storage handler
     * @return Result of the command as a String
     */
    public static String handle(String input, ArrayList<Task> tasklist, Storage storage) {
        StringBuilder output = new StringBuilder();

        try {
            input = input.toLowerCase().trim();

            if (input.equals(Config.CMD_CHEER)) {
                openCheerLink();
                output.append("Cheering GO! Link opened.\n");

            } else if (input.equals(Config.CMD_YES)) {
                output.append("Okay, but convince the oven it's not a time machine!\n");

            } else if (input.equals(Config.CMD_NO)) {
                output.append("Lilith is sad...\n");

            } else if (input.equals(Config.CMD_LIST)) {
                if (tasklist.isEmpty()) {
                    output.append("You're free!\n");
                } else {
                    for (int i = 0; i < tasklist.size(); i++) {
                        output.append((i + 1)).append(". ").append(tasklist.get(i)).append("\n");
                    }
                }

            } else if (input.startsWith(Config.CMD_FIND)) {
                String keyword = input.substring(Config.CMD_FIND.length()).trim();
                if (keyword.isEmpty()) {
                    output.append("Include which task you are looking for!\n");
                } else {
                    int count = 0;
                    for (Task task : tasklist) {
                        if (task.getTaskname().toLowerCase().contains(keyword)) {
                            count++;
                            output.append(count).append(". ").append(task).append("\n");
                        }
                    }
                    if (count == 0) {
                        output.append("No matching tasks found for \"").append(keyword).append("\".\n");
                    }
                }

            } else if (input.startsWith(Config.CMD_TODO)) {
                Task task = new Task(input.substring(Config.CMD_TODO.length()), null, null);
                task.setTask(Task.TaskType.ToDos);
                tasklist.add(task);
                storage.saveTasks(tasklist);
                output.append("Got it. I've added this task:\n").append(task).append("\n");

            } else if (input.startsWith(Config.CMD_DEADLINE)) {
                String[] parts = Parser.parseDeadlineInput(input.substring(Config.CMD_DEADLINE.length()));
                Task task = new Task(parts[0], null, parts[1]);
                task.setTask(Task.TaskType.Deadline);
                tasklist.add(task);
                storage.saveTasks(tasklist);
                output.append("Got it. I've added this task:\n").append(task).append("\n");

            } else if (input.startsWith(Config.CMD_EVENT)) {
                String[] parts = Parser.parseEventInput(input.substring(Config.CMD_EVENT.length()));
                Task task = new Task(parts[0], parts[1], parts[2]);
                task.setTask(Task.TaskType.Events);
                tasklist.add(task);
                storage.saveTasks(tasklist);
                output.append("Got it. I've added this task:\n").append(task).append("\n");

            } else if (input.startsWith(Config.CMD_MARK)) {
                int index = Integer.parseInt(input.substring(Config.CMD_MARK.length())) - 1;
                tasklist.get(index).mark();
                storage.saveTasks(tasklist);
                output.append("Nicely done! Good job!\n").append(tasklist.get(index)).append("\n");

            } else if (input.startsWith(Config.CMD_UNMARK)) {
                int index = Integer.parseInt(input.substring(Config.CMD_UNMARK.length())) - 1;
                tasklist.get(index).unmark();
                storage.saveTasks(tasklist);
                output.append("Make sure to finish it soon, ok?\n").append(tasklist.get(index)).append("\n");

            } else if (input.startsWith(Config.CMD_DELETE)
                    || input.startsWith(Config.CMD_DEL)
                    || input.startsWith(Config.CMD_REMOVE)) {
                int index = Integer.parseInt(input.replaceAll("^(delete|del|remove)\\s+", "")) - 1;
                Task removed = tasklist.remove(index);
                storage.saveTasks(tasklist);
                output.append("Ta-da! I have removed the task:\n").append(removed).append("\n");

            } else if (input.equals(Config.CMD_EMPTY_ALL)) {
                tasklist.clear();
                storage.saveTasks(tasklist);
                output.append("All tasks have been cleared!\n");

            } else if (input.equals(Config.CMD_BYE)) {
                output.append("Bye-bye! I will always be here.\n");

            } else {
                output.append("Lilith cannot find the task type...\n");
            }

        } catch (IndexOutOfBoundsException e) {
            output.append("That task does not exist!\n");
        } catch (Exception e) {
            output.append("Exception detected! Check format: use /by or /from /to for deadlines/events.\n");
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

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(Config.CHEER_LINK));
                System.out.println("Cheering operation, GO!");
            } catch (Exception e) {
                System.out.println("Failed to open site: " + e.getMessage());
            }
        } else {
            System.out.println("Desktop API not supported on this system.");
        }
    }
}


