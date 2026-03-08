package lilith.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lilith.storage.Storage;
import lilith.task.Task;

/**
 * Unit tests for Command class.
 * Tests both positive and negative scenarios for command handling.
 * Positive test: adding a simple todo task
 * Positive test: cheer command should return expected message
 * Negative test: searching for a task that doesn't exist
 * Negative test: marking a non-existent task, and cheer command response.
 */
class CommandTest {

    private ArrayList<Task> tasklist;
    private Storage storage;

    @BeforeEach
    void setUp() {
        tasklist = new ArrayList<>();
        storage = new Storage("./test.txt"); // use a test file
    }

    @Test
    void testAddTodoTask() {
        String input = "todo Read book";
        String output = Command.handle(input, tasklist, storage);

        assertEquals(1, tasklist.size(), "Tasklist should have 1 task after adding a todo");
        assertTrue(tasklist.get(0).getTaskname().equals("Read book"), "Task name should match input");
        assertTrue(output.contains("Okay! I've added this task"), "Output should confirm addition");
    }

    @Test
    void testFindTaskNotExists() {
        tasklist.add(new Task("Complete assignment", null, null));
        String input = "find homework";
        String output = Command.handle(input, tasklist, storage);

        assertTrue(output.contains("No matching tasks found"), "Should report no tasks found");
    }

    @Test
    void testMarkTaskOutOfBounds() {
        tasklist.add(new Task("Buy milk", null, null));
        String input = "mark 5";
        String output = Command.handle(input, tasklist, storage);

        assertTrue(output.contains("That task does not exist!"), "Should handle IndexOutOfBounds gracefully");
    }

    @Test
    void testCheerCommand() {
        String input = "cheer";
        String output = Command.handle(input, tasklist, storage);

        assertTrue(
            output.contains("Cheering operation GO!") || output.contains("Desktop API not supported")
            || output.contains("Invalid URL. Cannot open.")
            || output.contains("Failed to open cheer"),
            "Output should indicate cheer attempt was made"
        );
    }

    @Test
    void testUpdateTaskName() {
        Command.handle("todo Read book", tasklist, storage);

        String output = Command.handle("update 1 /name Study for exam", tasklist, storage);

        assertEquals("Study for exam", tasklist.get(0).getTaskname(),
            "Task name should be updated");
        assertTrue(output.contains("Task updated!"),
            "Output should confirm update");
    }

    @Test
    void testUpdateDeadlineDate() {
        Command.handle("deadline Submit report /by 2025-01-01", tasklist, storage);

        String output = Command.handle("update 1 /by 2025-06-15", tasklist, storage);

        assertTrue(output.contains("Task updated!"),
            "Output should confirm update");
        assertTrue(output.contains("Jun 15 2025"),
            "Updated deadline date should appear in output");
    }

    @Test
    void testUpdateOutOfBounds() {
        Command.handle("todo Read book", tasklist, storage);

        String output = Command.handle("update 99 /name New name", tasklist, storage);

        assertTrue(output.contains("That task does not exist!"),
            "Should handle out of bounds index gracefully");
    }

    @Test
    void testUpdateMissingIndex() {
        Command.handle("todo Read book", tasklist, storage);

        String output = Command.handle("update /name No index given", tasklist, storage);

        assertTrue(output.contains("Please provide a valid task number."),
            "Should catch NumberFormatException when index is missing");
    }
}

