package task;

public class Deadline extends Task {
    protected String description;
    protected boolean is_done;

    public char get_enum_icon() {
        return 'D';
    }

    public Deadline(String description) {
        super(description);
    }
}
