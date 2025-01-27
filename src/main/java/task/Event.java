package task;

public class Event extends Task {
    protected String from_, to_;

    public Event(String description, String from, String to) {
        super(description);
        from_ = from;
        to_ = to;
    }

    public char get_enum_icon() {
        return 'E';
    }

    public String get_description() {
        return String.format("%s (from: %s to: %s)", description_, from_, to_);
    }
}
