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
            else if (input.equalsIgnoreCase("list")) {
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

            else{
                Task taskname = new Task(input);
                tasklist.add(taskname);
                System.out.println("added:" + input);
            }
        }
        scanner.close();
    }
}

class Task{
    String taskname;
    boolean status;

    Task(String taskname){
        this.taskname = taskname;
        this.status = false;
    }

    void mark(){
        status = true;
    }

    void unmark(){
        status = false;
    }
    @Override
    public String toString() {
        return "[" + (status ? "X" : " ") + "] " + taskname;
    }
}
