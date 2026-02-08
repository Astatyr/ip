import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lilith {
    public static void main(String[] args) {
        System.out.println("Hello, I'm Lilith!");
        System.out.println("Would you like a strawberry cake?");

        Scanner scanner = new Scanner(System.in);
        //ArrayList<Task> tasklist = new ArrayList<>();

        Storage storage = new Storage("./data/lilith.txt");
        ArrayList<Task> tasklist = storage.loadTasks();
        if (!tasklist.isEmpty()){
            System.out.println("Loaded " + tasklist.size() + " tasks!");
        }

        while (true) {
            System.out.println("--------------------------------------------------------------");
            String input = scanner.nextLine();

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
                if(tasklist.isEmpty()){
                    System.out.println("You're free!");
                }
                for (int i = 0; i < tasklist.size(); i++){   
                    System.out.println((i+1) + ". " + tasklist.get(i));
                }
            }

            else if (input.startsWith("mark ")) {
                try {
                    int index = Integer.parseInt(input.substring(5)) - 1;
                    tasklist.get(index).mark();
                    storage.saveTasks(tasklist);
                    System.out.println("Nicely done! Good job!");
                    System.out.println(tasklist.get(index));
                } 
                catch (IndexOutOfBoundsException e) {
                    System.out.println("That task does not exist!");
                }
                catch (Exception e) {
                    System.out.println("Hey, write a proper number!");
                }
            }

            else if (input.startsWith("unmark ")) {
                try {
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    tasklist.get(index).unmark();
                    storage.saveTasks(tasklist);
                    System.out.println("Make sure to finish it soon, ok?");
                    System.out.println(tasklist.get(index));
                } 
                catch (IndexOutOfBoundsException e) {
                    System.out.println("That task does not exist!");
                }
                catch (Exception e) {
                    System.out.println("Hey, write a proper number!");
                }
                
            }

            else if (input.startsWith("todo ")) {
                input = input.substring(5);
                Task task = new Task(input, null, null);
                tasklist.add(task);
                storage.saveTasks(tasklist);
                task.setTask(Task.TaskType.ToDos);
                System.out.println("Got it. I've added this task:\n" + task);
                System.out.println("Now you have " + tasklist.size() + " task(s) in the list.");
            }

            else if (input.startsWith("deadline ")) {
                try {
                input = input.substring(9);
                String[] parts = input.split("/by");
                Task task = new Task(parts[0], null, parts[1]);
                tasklist.add(task);
                storage.saveTasks(tasklist);
                task.setTask(Task.TaskType.Deadline);
                System.out.println("Got it. I've added this task:\n" + task);
                System.out.println("Now you have " + tasklist.size() + " task(s) in the list.");
                } 
                catch (IndexOutOfBoundsException e) {
                    System.out.println("Please write the task properly! 'deadline <task name> /by <end time/date>'");
                }
                catch (Exception e) {
                }

            }

            else if (input.startsWith("event ")) {
                try {
                input = input.substring(6);
                String[] parts = input.split("/from");
                Task task;
                if (parts[0].contains("/to")){
                    String[] parts_sub = parts[0].split("/to");
                    task = new Task(parts_sub[0], parts[1], parts_sub[1]);
                }
                else{
                    String[] parts_sub = parts[1].split("/to");
                    task = new Task(parts[0], parts_sub[0], parts_sub[1]);
                }
                tasklist.add(task);
                storage.saveTasks(tasklist);
                task.setTask(Task.TaskType.Events);
                System.out.println("Got it. I've added this task:\n" + task);
                System.out.println("Now you have " + tasklist.size() + " task(s) in the list.");
                } 
                catch (IndexOutOfBoundsException e) {
                    System.out.println("Please write the task properly! 'event <task name> /from <start time/date> /to <end time/date>'");
                }
                catch (Exception e) {
                }
            }
            
            else if (input.startsWith("delete ")){
                try {
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    Task task = tasklist.get(index);
                    tasklist.remove(index);
                    storage.saveTasks(tasklist);
                    System.out.println("Ta-da! I have removed the task:");
                    System.out.println(task);
                    task = null;
                    System.out.println("Now you have " + tasklist.size() + " task(s) in the list.");
                }
                catch (IndexOutOfBoundsException e) {
                    System.out.println("That task does not exist!");
                }
                catch (Exception e) {
                    System.out.println("Hey, write a proper number!"); 
                }
            }

            else if (input.startsWith("todo") || input.startsWith("deadline") || input.startsWith("event") || input.startsWith("delete")){
                System.out.println("Hey! Fill in the task properly! ");
            }
            else{
                System.out.println("Lilith cannot find the task type... ");
            }
        }
        scanner.close();
    }
}

class Task{
    enum TaskType{
        ToDos, Deadline, Events
    }

    String taskname;
    boolean status;
    TaskType tasktype;
    String startdetail;
    String enddetail;

    Task(String taskname, String startdetail, String enddetail){
        this.taskname = taskname;
        this.status = false;
        this.tasktype = TaskType.ToDos;
        this.startdetail = startdetail;
        this.enddetail = enddetail;
    }

    public void setTask(TaskType input){
        tasktype = input;
    }

    void mark(){
        status = true;
    }

    void unmark(){
        status = false;
    }

    public String toFileString() {
        String typeLetter = switch (tasktype) {
            case ToDos -> "T";
            case Deadline -> "D";
            case Events -> "E";
        };

        String done = status ? "1" : "0";

        if (tasktype == TaskType.ToDos) {
            return typeLetter + " | " + done + " | " + taskname;
        }

        if (tasktype == TaskType.Deadline) {
            return typeLetter + " | " + done + " | " + taskname + " | " + enddetail;
        }

        // Events
        return typeLetter + " | " + done + " | " + taskname
                + " | " + startdetail + " | " + enddetail;
    }

    public static Task fromFileString(String line) {
        String[] parts = line.split("\\s*\\|\\s*");

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid task format");
        }

        String type = parts[0];
        boolean done = parts[1].equals("1");
        String name = parts[2];
        Task task;

        switch (type) {
            case "T" -> {
                task = new Task(name, null, null);
                task.setTask(TaskType.ToDos);
            }

            case "D" -> {
                if (parts.length < 4) {
                    throw new IllegalArgumentException("Deadline missing detail");
                }
                task = new Task(name, null, parts[3]);
                task.setTask(TaskType.Deadline);
            }

            case "E" -> {
                if (parts.length < 5) {
                    throw new IllegalArgumentException("Event missing details");
                }
                task = new Task(name, parts[3], parts[4]);
                task.setTask(TaskType.Events);
            }

            default -> throw new IllegalArgumentException("Unknown task type");
        }

        if (done) {
            task.mark();
        }

        return task;
    }


    @Override
    public String toString() {
        switch (tasktype){
            case ToDos -> {
                return "[T][" + (status ? "X" : " ") + "] " + taskname;
            }
            case Deadline -> {
                return "[D][" + (status ? "X" : " ") + "] " + taskname + "(by: " + enddetail + ")";
            }
            case Events -> {
                return "[E][" + (status ? "X" : " ") + "] " + taskname + "(from: " + startdetail + " to: " + enddetail + ")";
            }
        }
        return "[" + (status ? "X" : " ") + "] " + taskname;
    }
} 


class Storage {

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
//test branching