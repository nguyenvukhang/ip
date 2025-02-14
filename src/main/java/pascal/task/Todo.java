package pascal.task;

import java.time.LocalDate;
import java.util.Optional;

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

    /** Enum icon of a Todo Task. */
    public char getEnumIcon() {
        return 'T';
    }

    /** Description of a Todo Task. */
    public String getDescription() {
        return description;
    }

    /** Indicative date of a Todo Task. */
    public Optional<LocalDate> getDate() {
        return Optional.empty();
    }

    /** Serialize a Todo Task to save it to the filesystem. */
    public String serialize() {
        return description;
    }

    /** Deserialize a Todo Task from a String. */
    public Result<Task, Error> deserialize(String text) {
        return Result.ok(new Todo(text));
    }
}
