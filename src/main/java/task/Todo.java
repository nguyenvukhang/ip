package task;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    public char get_enum_icon() {
        return 'T';
    }

    public String get_description() {
        return description_;
    }
}
