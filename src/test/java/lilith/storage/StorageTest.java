package lilith.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import lilith.task.Task;

/**
 * Tests for Storage save/load behavior.
 */
public class StorageTest {

    @Test
    public void saveAndLoadTasks_roundTrip_success() {

        Storage storage = new Storage("./data/test-lilith.txt");

        ArrayList<Task> tasks = new ArrayList<>();

        Task t1 = new Task("read book", null, null);
        t1.setTask(Task.TaskType.ToDos);
        t1.mark();

        Task t2 = new Task("return book", null, "June 6th");
        t2.setTask(Task.TaskType.Deadline);

        tasks.add(t1);
        tasks.add(t2);

        // Save tasks
        storage.saveTasks(tasks);

        // Load back tasks
        ArrayList<Task> loaded = storage.loadTasks();

        assertEquals(2, loaded.size());
        assertEquals(t1.toString(), loaded.get(0).toString());
        assertEquals(t2.toString(), loaded.get(1).toString());
    }
}

