package pascal.task;

import pascal.result.Error;
import pascal.result.Result;

/**
 * A Todo.
 * A simple task that has only the text body.
 */
public class Todo extends Task {
    /** Empty constructor for inner use. */
    public Todo() {
        super("");
    }

    /** Create a Todo Task. */
    public Todo(String description) {
        super(description);
    }

    /** Enum icon of a Todo Task */
    public char get_enum_icon() {
        return 'T';
    }

    /** Description of a Todo Task */
    public String get_description() {
        return description_;
    }

    /** Serialize a Todo Task to save it to the filesystem. */
    public String serialize() {
        return description_;
    }

    /** Deserialize a Todo Task from a String. */
    public Result<Task, Error> deserialize(String text) {
        return Result.Ok(new Todo(text));
    }
}
