import java.util.ArrayList;
import java.util.Scanner;
public class Lilith {

    public static void main(String[] args) {
        System.out.println("Hello, I'm Lilith!");
        System.out.println("Would you like a strawberry cake?");

        planner();
    }


    public static void planner() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> Tasklist = new ArrayList<>();

        while (true) {
            System.out.println("--------------------------------------------------------------");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }
            if (input.equalsIgnoreCase("list")) {
                for (int i = 0; i < Tasklist.size(); i++){   
                    System.out.println((i+1) + ". " + Tasklist.get(i));
                }
            }

            else{
                Tasklist.add(input);
                System.out.println("added:" + input);
            }
        }
        scanner.close();
    }
}
