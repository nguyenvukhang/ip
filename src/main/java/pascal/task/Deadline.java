package pascal.task;

import java.time.LocalDate;
import java.util.Optional;
import pascal.common.Pair;
import pascal.common.Str;
import pascal.result.Error;
import pascal.result.Result;

/**
 * A Deadline'd Task.
 * A task that has a deadline.
 */
public class Deadline extends Task {
    protected LocalDate by_;

    /** Empty constructor for inner use. */
    public Deadline() {
        super("");
    }

    /** Create a Deadline Task. */
    public Deadline(String description, LocalDate by) {
        super(description);
        by_ = by;
    }

    /** Parse a Deadline Task from strings. */
    public static Result<Deadline, Error> of(String description, String by) {
        return parse_date(by).map(z -> new Deadline(description, z));
    }

    /** Enum icon of a Deadline Task */
    public char get_enum_icon() {
        return 'D';
    }

    /** Description of a Deadline Task */
    public String get_description() {
        return String.format("%s (by: %s)", description_, by_);
    }

    /** Serialize a Deadline Task to save it to the filesystem. */
    public String serialize() {
        return String.format("%s::%s", description_, by_);
    }

    /** Deserialize a Deadline Task from a String. */
    public Result<Task, Error> deserialize(String text) {
        Str x = new Str(text);
        Optional<Pair<Str, Str>> opt = x.split_once("::");
        if (opt.isEmpty()) {
            return Result.Err(Error.other("Error in parsing an `Deadline`."));
        }
        String description = opt.get().left.inner();
        String by = opt.get().right.inner();
        return parse_date(by).map(z -> new Deadline(description, z));
    }
}
