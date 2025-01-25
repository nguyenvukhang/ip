package task;

public class Todo extends Task {
    protected String description;
    protected boolean is_done;

    public char get_enum_icon() {
        return 'T';
    }

    public String get_description() {
        return description_;
    }

    public Todo(String description) {
        super(description);
    }
}
