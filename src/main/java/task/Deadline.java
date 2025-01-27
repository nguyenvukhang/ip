package task;

public class Deadline extends Task {
    protected String by_;

    public Deadline(String description, String by) {
        super(description);
        by_ = by;
    }

    public char get_enum_icon() {
        return 'D';
    }

    public String get_description() {
        return String.format("%s (by: %s)", description_, by_);
    }
}
