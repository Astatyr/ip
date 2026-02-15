package lilith.task;

import java.time.LocalDateTime;

import lilith.parser.Parser;

/**
 * Task class, to represent tasks.
 */
public class Task {

    /**
     * Enum representing different task types.
     */
    public enum TaskType {
        ToDos,
        Deadline,
        Events
    }

    private String taskname;
    private boolean status;
    private TaskType tasktype;
    private String startdetail;
    private String enddetail;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    /**
     * Constructs a Task with optional start and end details.
     *
     * @param taskname Task description.
     * @param startdetail Start date/time detail (optional).
     * @param enddetail End date/time detail (optional).
     */
    public Task(String taskname, String startdetail, String enddetail) {
        this.taskname = taskname;
        this.status = false;
        this.tasktype = TaskType.ToDos;
        this.startdetail = startdetail;
        this.enddetail = enddetail;

        try {
            if (startdetail != null && !startdetail.isEmpty()) {
                this.startDateTime = Parser.parseDateTime(startdetail);
            }

            if (enddetail != null && !enddetail.isEmpty()) {
                this.endDateTime = Parser.parseDateTime(enddetail);
            }

        } catch (Exception e) {
            this.startDateTime = null;
            this.endDateTime = null;
        }
    }

    /**
     * Sets the task type.
     *
     * @param input Task type.
     */
    public void setTask(TaskType input) {
        this.tasktype = input;
    }

    /**
     * Marks the task as done.
     */
    public void mark() {
        this.status = true;
    }

    /**
     * Marks the task as not done.
     */
    public void unmark() {
        this.status = false;
    }

    /**
     * Returns file representation of this task.
     *
     * @return Task formatted for saving.
     */
    public String toFileString() {
        String typeLetter;

        switch (tasktype) {
        case ToDos:
            typeLetter = "T";
            break;
        case Deadline:
            typeLetter = "D";
            break;
        case Events:
            typeLetter = "E";
            break;
        default:
            typeLetter = "?";
            break;
        }

        String done = status ? "1" : "0";

        if (tasktype == TaskType.ToDos) {
            return typeLetter + " | " + done + " | " + taskname;
        }

        if (tasktype == TaskType.Deadline) {
            return typeLetter + " | " + done + " | " + taskname
                    + " | " + enddetail;
        }

        return typeLetter + " | " + done + " | " + taskname
                + " | " + startdetail + " | " + enddetail;
    }

    /**
     * Converts a file line into a Task object.
     *
     * @param line Line from save file.
     * @return Parsed Task object.
     */
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
        case "T":
            task = new Task(name, null, null);
            task.setTask(TaskType.ToDos);
            break;

        case "D":
            if (parts.length < 4) {
                throw new IllegalArgumentException("Deadline missing detail");
            }
            task = new Task(name, null, parts[3]);
            task.setTask(TaskType.Deadline);
            break;

        case "E":
            if (parts.length < 5) {
                throw new IllegalArgumentException("Event missing details");
            }
            task = new Task(name, parts[3], parts[4]);
            task.setTask(TaskType.Events);
            break;

        default:
            throw new IllegalArgumentException("Unknown task type");
        }

        if (done) {
            task.mark();
        }

        return task;
    }

    /**
     * Returns formatted display string for the user.
     *
     * @return User-readable task string.
     */
    @Override
    public String toString() {

        switch (tasktype) {

        case ToDos:
            return "[T][" + (status ? "X" : " ") + "] " + taskname;

        case Deadline:
            String formattedEnd =
                    endDateTime != null
                            ? Parser.formatDateTime(endDateTime)
                            : enddetail;

            return "[D][" + (status ? "X" : " ") + "] "
                    + taskname + " (by: " + formattedEnd + ")";

        case Events:
            String formattedStart =
                    startDateTime != null
                            ? Parser.formatDateTime(startDateTime)
                            : startdetail;

            String formattedEndEvent =
                    endDateTime != null
                            ? Parser.formatDateTime(endDateTime)
                            : enddetail;

            return "[E][" + (status ? "X" : " ") + "] "
                    + taskname + " (from: " + formattedStart
                    + " to: " + formattedEndEvent + ")";

        default:
            return "[" + (status ? "X" : " ") + "] " + taskname;
        }
    }

    /**
     * Getters.
     */

    public String getTaskname() {
        return taskname;
    }

    public boolean isDone() {
        return status;
    }

    public TaskType getTasktype() {
        return tasktype;
    }
}


