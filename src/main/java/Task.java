public class Task {
    protected String description;
    protected boolean is_done;

    public Task(String description) {
        this.description = description;
        this.is_done = false;
    }

    public void mark_as_done() {
        is_done = true;
    }

    public void mark_as_not_done() {
        is_done = false;
    }

    public String get_status_icon() {
        return is_done ? "X" : " "; // mark done task with X
    }

    public String toString() {
        return description;
    }
}
