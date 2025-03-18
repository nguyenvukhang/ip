package pascal.task;

import java.time.LocalDate;
import java.util.Optional;

import pascal.common.Pair;
import pascal.common.Str;
import pascal.result.Error;
import pascal.result.Result;

/**
 * A Deadline'd Task. A task that has a deadline.
 */
public class Deadline extends Task {
    protected LocalDate byDate;

    /** Empty constructor for inner use. */
    public Deadline() {
        super("");
    }

    /** Create a Deadline Task. */
    public Deadline(String description, LocalDate by) {
        super(description);
        byDate = by;
    }

    /** Parse a Deadline Task from strings. */
    public static Result<Deadline, Error> of(String description, String by) {
        return parseDate(by).map(z -> new Deadline(description, z));
    }

    /** Enum icon of a Deadline Task. */
    public char getEnumIcon() {
        return 'D';
    }

    /** Description of a Deadline Task. */
    public String getDescription() {
        return String.format("%s (by: %s)", description, byDate);
    }

    /** Indicative date of a Deadline Task. */
    public Optional<LocalDate> getDate() {
        return Optional.of(byDate);
    }

    /** Serialize a Deadline Task to save it to the filesystem. */
    public String serialize() {
        return String.format("%s::%s", description, byDate);
    }

    /** Deserialize a Deadline Task from a String. */
    public Result<Task, Error> deserialize(String text) {
        Str x = new Str(text);
        Optional<Pair<Str, Str>> opt = x.splitOnce("::");
        if (opt.isEmpty()) {
            return Result.err(Error.other("Error in parsing an `Deadline`."));
        }
        String description = opt.get().left().inner();
        String by = opt.get().right().inner();
        return parseDate(by).map(z -> new Deadline(description, z));
    }
}
