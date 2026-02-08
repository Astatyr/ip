public class Task{
    enum TaskType{
        ToDos, Deadline, Events
    }

    String taskname;
    boolean status;
    TaskType tasktype;
    String startdetail;
    String enddetail;

    Task(String taskname, String startdetail, String enddetail){
        this.taskname = taskname;
        this.status = false;
        this.tasktype = TaskType.ToDos;
        this.startdetail = startdetail;
        this.enddetail = enddetail;
    }

    public void setTask(TaskType input){
        tasktype = input;
    }

    void mark(){
        status = true;
    }

    void unmark(){
        status = false;
    }

    public String toFileString() {
        String typeLetter = switch (tasktype) {
            case ToDos -> "T";
            case Deadline -> "D";
            case Events -> "E";
        };

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
            case "T" -> {
                task = new Task(name, null, null);
                task.setTask(TaskType.ToDos);
            }

            case "D" -> {
                if (parts.length < 4) {
                    throw new IllegalArgumentException("Deadline missing detail");
                }
                task = new Task(name, null, parts[3]);
                task.setTask(TaskType.Deadline);
            }

            case "E" -> {
                if (parts.length < 5) {
                    throw new IllegalArgumentException("Event missing details");
                }
                task = new Task(name, parts[3], parts[4]);
                task.setTask(TaskType.Events);
            }

            default -> throw new IllegalArgumentException("Unknown task type");
        }

        if (done) {
            task.mark();
        }

        return task;
    }


    @Override
    public String toString() {
        switch (tasktype){
            case ToDos -> {
                return "[T][" + (status ? "X" : " ") + "] " + taskname;
            }
            case Deadline -> {
                return "[D][" + (status ? "X" : " ") + "] " + taskname + "(by: " + enddetail + ")";
            }
            case Events -> {
                return "[E][" + (status ? "X" : " ") + "] " + taskname + "(from: " + startdetail + " to: " + enddetail + ")";
            }
        }
        return "[" + (status ? "X" : " ") + "] " + taskname;
    }
} 
