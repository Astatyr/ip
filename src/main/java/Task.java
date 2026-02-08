import java.time.LocalDateTime;

public class Task {
    enum TaskType { ToDos, Deadline, Events }

    String taskname;
    boolean status;
    TaskType tasktype;

    // Original string fields (optional, could be removed)
    String startdetail;
    String enddetail;

    // Parsed date fields for standardized output
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;

    Task(String taskname, String startdetail, String enddetail) {
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
        } catch (Exception ignored) {}
    }

    public void setTask(TaskType input) {
        tasktype = input;
    }

    void mark(){
        status = true;
    }

    void unmark(){
        status = false;
    }

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

        // Events
        return typeLetter + " | " + done + " | " + taskname
                + " | " + startdetail + " | " + enddetail;
    }

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

