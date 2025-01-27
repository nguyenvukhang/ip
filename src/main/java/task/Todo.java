package task;

import result.Error;
import result.Result;

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

    public Result<Task, Error> deserialize(String text) {
        return Result.Ok(new Todo(text));
    }
}
