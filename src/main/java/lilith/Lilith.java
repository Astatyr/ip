package lilith;

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

import lilith.parser.Parser;
import lilith.storage.Storage;
import lilith.task.Task;

/**
 * Lilith class, main application class.
 */
public class Lilith {

    /**
     * Links, change as necessary.
     */
    private static final String CHEER_LINK =
            "https://www.youtube.com/watch?v=FAmojODvK64&list=RDFAmojODvK64&start_radio=1";

    private static final String DATA_PATH = "./LilithData/lilith.txt";

    /**
     * Initializes Lilith.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        System.out.println("Hello, I'm Lilith!");
        System.out.println("Would you like a strawberry cake?");

        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage(DATA_PATH);
        ArrayList<Task> tasklist = storage.loadTasks();

        if (!tasklist.isEmpty()) {
            System.out.println("Loaded " + tasklist.size() + " tasks!");
        }

        while (true) {
            System.out.println("--------------------------------------------------------------");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("Bye-bye! I will always be here when you need me!");
                break;
            } else {
                handleCommand(input, tasklist, storage);
            }
        }

        scanner.close();
    }

    /**
     * Searches tasks containing the keyword and prints them.
     *
     * @param keyword Keyword to search for.
     * @param tasklist List of tasks.
     */
    private static void findTasks(String keyword, ArrayList<Task> tasklist) {
        System.out.println("Here are the matching tasks in your list:");

        int count = 0;

        for (int i = 0; i < tasklist.size(); i++) {
            Task task = tasklist.get(i);

            if (task.getTaskname().toLowerCase().contains(keyword.toLowerCase())) {
                count++;
                System.out.println(count + ". " + task);
            }
        }

        if (count == 0) {
            System.out.println("No matching tasks found for \"" + keyword + "\".");
        }
    }

    /**
     * Opens the Cheer link.
     */
    private static void openCheerLink() {
        if (!CHEER_LINK.startsWith("https://www.youtube.com/")) {
            System.out.println("Invalid URL. Cannot open.");
            return;
        }

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(CHEER_LINK));
                System.out.println("Cheering operation, GO!");
            } catch (Exception e) {
                System.out.println("Failed to open site: " + e.getMessage());
            }
        } else {
            System.out.println("Desktop API not supported on this system.");
        }
    }

    /**
     * User command handling.
     *
     * @param input User input string.
     * @param tasklist Task list.
     * @param storage Storage handler.
     */
    private static void handleCommand(
            String input,
            ArrayList<Task> tasklist,
            Storage storage) {

        try {
            input = input.toLowerCase().trim();

            if (input.equals("cheer")) {
                openCheerLink();

            } else if (input.equals("yes")) {
                System.out.println(
                        "Okay, but you'll need to convince the oven that it is not a time machine!"
                );

            } else if (input.equals("no")) {
                System.out.println("Lilith is sad...");

            } else if (input.equals("list")) {

                if (tasklist.isEmpty()) {
                    System.out.println("You're free!");
                } else {
                    for (int i = 0; i < tasklist.size(); i++) {
                        System.out.println((i + 1) + ". " + tasklist.get(i));
                    }
                }

            } else if (input.startsWith("find ")) {

                String keyword = input.substring(5).trim();

                if (keyword.isEmpty()) {
                    System.out.println("Include which task are you looking for!");
                } else {
                    findTasks(keyword, tasklist);
                }

            } else if (input.startsWith("todo ")) {

                Task task = new Task(input.substring(5), null, null);
                task.setTask(Task.TaskType.ToDos);

                tasklist.add(task);
                storage.saveTasks(tasklist);

                System.out.println("Got it. I've added this task:\n" + task);

            } else if (input.startsWith("deadline ")) {

                String[] parts = Parser.parseDeadlineInput(input.substring(9));
                Task task = new Task(parts[0], null, parts[1]);

                task.setTask(Task.TaskType.Deadline);

                tasklist.add(task);
                storage.saveTasks(tasklist);

                System.out.println("Got it. I've added this task:\n" + task);

            } else if (input.startsWith("event ")) {

                String[] parts = Parser.parseEventInput(input.substring(6));
                Task task = new Task(parts[0], parts[1], parts[2]);

                task.setTask(Task.TaskType.Events);

                tasklist.add(task);
                storage.saveTasks(tasklist);

                System.out.println("Got it. I've added this task:\n" + task);

            } else if (input.startsWith("mark ")) {

                int index = Integer.parseInt(input.substring(5)) - 1;

                tasklist.get(index).mark();
                storage.saveTasks(tasklist);

                System.out.println(
                        "Nicely done! Good job!\n" + tasklist.get(index)
                );

            } else if (input.startsWith("unmark ")) {

                int index = Integer.parseInt(input.substring(7)) - 1;

                tasklist.get(index).unmark();
                storage.saveTasks(tasklist);

                System.out.println(
                        "Make sure to finish it soon, ok?\n" + tasklist.get(index)
                );

            } else if (input.startsWith("delete ")
                    || input.startsWith("del ")
                    || input.startsWith("remove ")) {

                int index = Integer.parseInt(input.substring(7)) - 1;

                Task removed = tasklist.remove(index);
                storage.saveTasks(tasklist);

                System.out.println("Ta-da! I have removed the task:\n" + removed);

            } else if (input.equals("/emptyall")) {

                tasklist.clear();
                storage.saveTasks(tasklist);

                System.out.println("All tasks have been cleared!");

            } else {
                System.out.println("Lilith cannot find the task type...");
            }

        } catch (IndexOutOfBoundsException e) {
            System.out.println("That task does not exist!");

        } catch (Exception e) {
            System.out.println(
                    "Exception detected! Check format: use /by or /from /to for deadlines/events."
            );
        }
    }
}
