package task;

import common.Pair;
import common.Str;
import java.util.Optional;
import result.Error;
import result.Result;

public class Event extends Task {
    protected String from_, to_;

    /** Empty constructor for inner use. */
    public Event() {
        super("");
    }

    public Event(String description, String from, String to) {
        super(description);
        from_ = from;
        to_ = to;
    }

    public char get_enum_icon() {
        return 'E';
    }

    public String get_description() {
        return String.format("%s (from: %s to: %s)", description_, from_, to_);
    }

    public String serialize() {
        return String.format("%s::%s::%s", description_, from_, to_);
    }

    public Result<Task, Error> deserialize(String text) {
        Str x = new Str(text);
        Optional<Pair<Str, Str>> opt;
        opt = x.split_once("::");
        if (opt.isEmpty()) {
            return Result.Err(Error.other("Error in parsing an `Event`."));
        }

        String description = opt.get().left.inner();
        opt = opt.get().right.split_once("::");
        if (opt.isEmpty()) {
            return Result.Err(Error.other("Error in parsing an `Event`."));
        }

        Str from = opt.get().left;
        Str to = opt.get().right;
        return Result.Ok(new Event(description, from.inner(), to.inner()));
    }
}
