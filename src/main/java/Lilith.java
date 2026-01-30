import java.util.ArrayList;
import java.util.Scanner;
public class Lilith {

    public static void main(String[] args) {
        System.out.println("Hello, I'm Lilith!");
        System.out.println("Would you like a strawberry cake?");

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasklist = new ArrayList<>();

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
                    System.out.println("You have not added any task yet.");
                }
                for (int i = 0; i < tasklist.size(); i++){   
                    System.out.println((i+1) + ". " + tasklist.get(i));
                }
            }

            else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5)) - 1;
                tasklist.get(index).mark();
                System.out.println("Nicely done! Good job!");
                System.out.println(tasklist.get(index));

            }

            else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                tasklist.get(index).unmark();
                System.out.println("Make sure to finish it soon, ok?");
                System.out.println(tasklist.get(index));
            }

            else if (input.startsWith("todo ")) {
                input = input.substring(5);
                Task task = new Task(input, null, null);
                tasklist.add(task);
                task.setTask(Task.TaskType.ToDos);
                System.out.println("Got it. I've added this task:\n" + task);
                System.err.println("Now you have " + tasklist.size() + " task(s) in the list.");
            }

            else if (input.startsWith("deadline ")) {
                try {
                input = input.substring(9);
                String[] parts = input.split("/by");
                Task task = new Task(parts[0], null, parts[1]);
                tasklist.add(task);
                task.setTask(Task.TaskType.Deadline);
                System.out.println("Got it. I've added this task:\n" + task);
                System.err.println("Now you have " + tasklist.size() + " task(s) in the list.");
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
                task.setTask(Task.TaskType.Events);
                System.out.println("Got it. I've added this task:\n" + task);
                System.err.println("Now you have " + tasklist.size() + " task(s) in the list.");
                } 
                catch (IndexOutOfBoundsException e) {
                    System.out.println("Please write the task properly! 'event <task name> /from <start time/date> /to <end time/date>'");
                }
                catch (Exception e) {
                }

            }

            else if (input.startsWith("todo") || input.startsWith("deadline") || input.startsWith("event")){
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

    @Override
    public String toString() {
        switch (tasktype){
            case ToDos:
                return "[T][" + (status ? "X" : " ") + "] " + taskname;
            case Deadline:
                return "[D][" + (status ? "X" : " ") + "] " + taskname + "(by: " + enddetail + ")";
            case Events:
                return "[E][" + (status ? "X" : " ") + "] " + taskname + "(from: " + startdetail + " to: " + enddetail + ")";
        }
        return "[" + (status ? "X" : " ") + "] " + taskname;
    }
}

//ToDo: 'event /from   /to', 
