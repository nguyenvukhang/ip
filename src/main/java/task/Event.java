package task;

public class Event extends Task {
    protected String description;
    protected boolean is_done;

    public char get_enum_icon() {
        return 'E';
    }

    public Event(String description) {
        super(description);
    }
}
