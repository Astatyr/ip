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
        assertTrue(output.contains("Got it. I've added this task"), "Output should confirm addition");
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

        assertTrue(output.contains("Cheering GO! Link opened"), "Output should indicate cheer link opened");
    }
}

