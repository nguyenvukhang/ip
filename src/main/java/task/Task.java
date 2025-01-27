package task;

public abstract class Task {
    protected String description_;
    protected boolean is_done_;

    public Task(String description) {
        description_ = description;
        is_done_ = false;
    }

    public void mark_as_done() {
        is_done_ = true;
    }

    public void mark_as_not_done() {
        is_done_ = false;
    }

    public char get_status_icon() {
        return is_done_ ? 'X' : ' '; // mark done task with X
    }

    abstract public char get_enum_icon();

    abstract public String get_description();

    @Override
    public String toString() {
        return String.format("[%s][%s] %s", get_enum_icon(), get_status_icon(),
                             get_description());
    }
}
