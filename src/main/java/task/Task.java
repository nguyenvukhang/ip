package task;

public abstract class Task {
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

    public char get_status_icon() {
        return is_done ? 'X' : ' '; // mark done task with X
    }

    abstract public char get_enum_icon();

    public String toString() {
        return String.format("[%s][%s] %s", get_enum_icon(), get_status_icon(),
                             description);
    }
}
