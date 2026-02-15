package lilith;

import java.util.ArrayList;
import java.util.Scanner;

import lilith.command.Command;
import lilith.config.Config;
import lilith.storage.Storage;
import lilith.task.Task;

/**
 * Lilith CLI application.
 * Uses the same backend as GUI, just outputs to console.
 */
public class LilithCli {

    public static void main(String[] args) {
        System.out.println("Hello, I'm Lilith!");
        System.out.println("Would you like a strawberry cake?");

        Storage storage = new Storage(Config.DATA_PATH.toString());
        ArrayList<Task> tasklist = storage.loadTasks();

        if (!tasklist.isEmpty()) {
            System.out.println("Loaded " + tasklist.size() + " tasks!");
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("--------------------------------------------------------------");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("Bye-bye! I will always be here when you need me!");
                break;
            }

            String response = Command.handle(input, tasklist, storage);
            System.out.print(response);
        }

        scanner.close();
    }
}

