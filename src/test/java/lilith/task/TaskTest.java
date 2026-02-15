package lilith.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void fromFileString_todoTask_parsesCorrectly() {
        Task task = Task.fromFileString("T | 1 | read book");

        assertEquals("[T][X] read book", task.toString());
    }

    @Test
    public void fromFileString_deadlineTask_parsesCorrectly() {
        Task task = Task.fromFileString("D | 0 | return book | June 6th");

        assertEquals("[D][ ] return book (by: June 6th)", task.toString());
    }

    @Test
    public void fromFileString_invalidLine_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Task.fromFileString("INVALID DATA");
        });
    }
}


