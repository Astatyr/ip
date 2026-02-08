import java.util.ArrayList;
import java.util.Scanner;

public class Lilith {

    public static void main(String[] args) {
        System.out.println("Hello, I'm Lilith!");
        System.out.println("Would you like a strawberry cake?");

        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage("./LilithData/lilith.txt");
        ArrayList<Task> tasklist = storage.loadTasks();

        if (!tasklist.isEmpty()) {
            System.out.println("Loaded " + tasklist.size() + " tasks!");
        }
        System.out.println("For Debugging - Current working directory: " + System.getProperty("user.dir"));

        while (true) {
            System.out.println("--------------------------------------------------------------");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("Bye-bye! I will always be here when you need me!");
                break;
            } 
            else if (input.equalsIgnoreCase("yes")) {
                System.out.println("Okay, but you'll need to convince the oven that it is not a time machine!");
            } 
            else if (input.equalsIgnoreCase("no")) {
                System.out.println("Lilith is sad...");
            } 
            else if (input.equalsIgnoreCase("list")) {
                if (tasklist.isEmpty()) System.out.println("You're free!");
                else for (int i = 0; i < tasklist.size(); i++)
                    System.out.println((i + 1) + ". " + tasklist.get(i));
            } 
            else if (input.toLowerCase().startsWith("find ")) {
                String keyword = input.substring(5).trim();
                if (keyword.isEmpty()) {
                    System.out.println("Please provide a keyword to search for!");
                } else {
                    findTasks(keyword, tasklist);
                }
            }
            else {
                handleCommand(input, tasklist, storage);
            }
        }

        scanner.close();
    }
    /**
     * Searches tasks containing the keyword and prints them.
     */
    private static void findTasks(String keyword, ArrayList<Task> tasklist) {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the matching tasks in your list:");

        int count = 0;
        for (int i = 0; i < tasklist.size(); i++) {
            Task task = tasklist.get(i);
            if (task.taskname.toLowerCase().contains(keyword.toLowerCase())) {
                count++;
                System.out.println((count) + ". " + task);
            }
        }

        if (count == 0) {
            System.out.println("No matching tasks found for \"" + keyword + "\".");
        }
        System.out.println("____________________________________________________________");
    }

    private static void handleCommand(String input, ArrayList<Task> tasklist, Storage storage) {
        try {
            if (input.toLowerCase().startsWith("todo ")) {
                Task task = new Task(input.substring(5), null, null);
                task.setTask(Task.TaskType.ToDos);
                tasklist.add(task);
                storage.saveTasks(tasklist);
                System.out.println("Got it. I've added this task:\n" + task);
            } 
            else if (input.toLowerCase().startsWith("deadline ")) {
                String[] parts = Parser.parseDeadlineInput(input.substring(9));
                Task task = new Task(parts[0], null, parts[1]); 
                task.setTask(Task.TaskType.Deadline);
                tasklist.add(task);
                storage.saveTasks(tasklist);
                System.out.println("Got it. I've added this task:\n" + task);
            } 
            else if (input.toLowerCase().startsWith("event ")) {
                String[] parts = Parser.parseEventInput(input.substring(6));
                Task task = new Task(parts[0], parts[1], parts[2]); 
                task.setTask(Task.TaskType.Events);
                tasklist.add(task);
                storage.saveTasks(tasklist);
                System.out.println("Got it. I've added this task:\n" + task);
            } 
            else if (input.toLowerCase().startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5)) - 1;
                tasklist.get(index).mark();
                storage.saveTasks(tasklist);
                System.out.println("Nicely done! Good job!\n" + tasklist.get(index));
            } 
            else if (input.toLowerCase().startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                tasklist.get(index).unmark();
                storage.saveTasks(tasklist);
                System.out.println("Make sure to finish it soon, ok?\n" + tasklist.get(index));
            } 
            else if (input.toLowerCase().startsWith("delete ")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                Task removed = tasklist.remove(index);
                storage.saveTasks(tasklist);
                System.out.println("Ta-da! I have removed the task:\n" + removed);
            } 
            else if (input.equalsIgnoreCase("/emptyall")) {
                tasklist.clear();
                storage.saveTasks(tasklist);
                System.out.println("All tasks have been cleared!");
            }
            else {
                System.out.println("Lilith cannot find the task type...");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("That task does not exist!");
        } catch (Exception e) {
            System.out.println("Invalid input. Check format or use /by or /from /to for deadlines/events.");
        }
    }
}



//test branching