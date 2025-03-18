package pascal.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import pascal.result.Error;
import pascal.result.Result;

/**
 * A Task. An abstract class that describes the general idea of a Task.
 */
public abstract class Task {
    protected static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected static LocalDate emptyDate = LocalDate.of(1, 1, 1);

    protected String description;
    protected boolean isDone;

    /** Create a Task from a description. */
    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    /** Mark a Task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Mark a Task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Obtains a status icon of the Task as a single character. Depends on if it's
     * done or not.
     */
    public char getStatusIcon() {
        return isDone ? 'X' : ' '; // mark done task with X
    }

    /** A date parser for internal use during parsing in subclasses. */
    protected static Result<LocalDate, Error> parseDate(String text) {
        try {
            return Result.ok(LocalDate.parse(text, dateFormat));
        } catch (DateTimeParseException e) {
            return Result.err(Error.other("Error parsing datetime: %s", e));
        }
    }

    /**
     * Require subclasses to have an enum icon. Used for serializing, deserializing,
     * and displaying.
     */
    abstract char getEnumIcon();

    /**
     * Require subclasses override how to display themselves. Used for displaying.
     */
    abstract String getDescription();

    /** Gets an indicative date, if any. */
    abstract Optional<LocalDate> getDate();

    /** Require subclasses to show how to serialize themselves. */
    abstract String serialize();

    /** Require subclasses to show how to deserialize themselves. */
    abstract Result<Task, Error> deserialize(String text);

    @Override
    public String toString() {
        return String.format("[%s][%s] %s", getEnumIcon(), getStatusIcon(), getDescription());
    }
}
