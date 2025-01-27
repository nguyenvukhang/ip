package task;

import common.Pair;
import common.Str;
import java.time.LocalDate;
import java.util.Optional;
import result.Error;
import result.Result;

public class Deadline extends Task {
    protected LocalDate by_;

    /** Empty constructor for inner use. */
    public Deadline() {
        super("");
    }

    public Deadline(String description, LocalDate by) {
        super(description);
        by_ = by;
    }

    public static Result<Deadline, Error> of(String description, String by) {
        return parse_date(by).map(z -> new Deadline(description, z));
    }

    public char get_enum_icon() {
        return 'D';
    }

    public String get_description() {
        return String.format("%s (by: %s)", description_, by_);
    }

    public String serialize() {
        return String.format("%s::%s", description_, by_);
    }

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
