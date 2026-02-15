package lilith.task;

import lilith.parser.Parser;
import java.time.LocalDateTime;

/**
 *Task class, to represent tasks.
 */
public class Task {
    public enum TaskType { 
        ToDos, Deadline, Events 
    }

    public String taskname;
    boolean status;
    TaskType tasktype;
    String startdetail;
    String enddetail;
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;

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
        } 
        catch (Exception ignored) {}
    }

    /**
     * Sets task type
     */
    public void setTask(TaskType input) {
        tasktype = input;
    }

    /**
     * Mark if a task is done.
     */
    public void mark(){
        status = true;
    }

    /**
     * Unmark if a task is not done.
     */
    public void unmark(){
        status = false;
    }

    /**
     * Format output for txt file.
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
        }

        String done = status ? "1" : "0";

        if (tasktype == TaskType.ToDos) {
            return typeLetter + " | " + done + " | " + taskname;
        }

        if (tasktype == TaskType.Deadline) {
            return typeLetter + " | " + done + " | " + taskname + " | " + enddetail;
        }

        //else Events
        return typeLetter + " | " + done + " | " + taskname
                + " | " + startdetail + " | " + enddetail;
    }

    /**
     * Convert string (from file read) to Task object.
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
                if (parts.length < 4) throw new IllegalArgumentException("Deadline missing detail");
                task = new Task(name, null, parts[3]);
                task.setTask(TaskType.Deadline);
                break;
            case "E":
                if (parts.length < 5) throw new IllegalArgumentException("Event missing details");
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
     * Format how tasks are displayed for user.
     */
    @Override
    public String toString() {
        switch (tasktype) {
            case ToDos:
                return "[T][" + (status ? "X" : " ") + "] " + taskname;
            case Deadline:
                String formattedEnd = endDateTime != null ? Parser.formatDateTime(endDateTime) : enddetail;
                return "[D][" + (status ? "X" : " ") + "] " + taskname + " (by: " + formattedEnd + ")";
            case Events:
                String formattedStart = startDateTime != null ? Parser.formatDateTime(startDateTime) : startdetail;
                String formattedEndEvent = endDateTime != null ? Parser.formatDateTime(endDateTime) : enddetail;
                return "[E][" + (status ? "X" : " ") + "] " + taskname + " (from: " + formattedStart + " to: " + formattedEndEvent + ")";
            default:
                return "[" + (status ? "X" : " ") + "] " + taskname;
        }
    }
}

