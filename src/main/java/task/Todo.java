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

    public String serialize() {
        return description_;
    }

    public Task deserialize(String text) {
        return new Todo(text);
    }
}
