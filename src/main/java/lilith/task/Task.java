package lilith.task;

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

    /**
     * Variables used. Do not make this final, in case there are future commands
     * that can modify these values.
     */
    private String taskname;
    private boolean status;
    private TaskType tasktype;

    private String startdetail;
    private String enddetail;

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

        assert tasktype != null : "Task type should never be null";
    }

    /**
     * Sets the task type.
     *
     * @param taskTypeInput Task type.
     */
    public void setTask(TaskType taskTypeInput) {
        assert taskTypeInput != null : "Task type cannot be null";
        this.tasktype = taskTypeInput;
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
     * Sets the task name.
     */
    public void setTaskName(String taskname) {
        this.taskname = taskname;
    }

    /**
     * Sets the task start detail.
     */
    public void setStartDetail(String startdetail) {
        this.startdetail = startdetail;
    }

    /**
     * Sets the task end detail.
     */
    public void setEndDetail(String enddetail) {
        this.enddetail = enddetail;
    }

    /**
     * Returns file representation of this task.
     *
     * @return Task formatted for saving.
     */
    public String toFileString() {
        assert tasktype != null : "Task type must not be null";

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
            assert false : "Unexpected task type: " + tasktype;
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

        assert line != null : "Saved task line should not be null";

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
            return "[D][" + (status ? "X" : " ") + "] "
                    + taskname + " (by: " + formatDetail(enddetail) + ")";

        case Events:
            return "[E][" + (status ? "X" : " ") + "] "
                    + taskname + " (from: " + formatDetail(startdetail)
                    + " to: " + formatDetail(enddetail) + ")";

        default:
            return "[" + (status ? "X" : " ") + "] " + taskname;
        }
    }

    /**
     * Formats a date/time string for output if possible.
     *
     * @param detail Raw string detail.
     * @return Formatted string or original if parsing fails.
     */
    private String formatDetail(String detail) {

        if (detail == null) {
            return "";
        }

        try {
            return Parser.formatDateTime(Parser.parseDateTime(detail));

        } catch (Exception e) {
            return detail;
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
