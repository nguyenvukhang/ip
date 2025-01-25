package task;

public class Todo extends Task {
    protected String description;
    protected boolean is_done;

    public char get_enum_icon() {
        return 'T';
    }

    public Todo(String description) {
        super(description);
    }
}
