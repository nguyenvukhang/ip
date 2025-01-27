package task;

import common.Pair;
import common.Str;
import java.time.LocalDate;
import java.util.Optional;
import result.Error;
import result.Result;

public class Event extends Task {
    protected LocalDate from_, to_;

    /** Empty constructor for inner use. */
    public Event() {
        super("");
    }

    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        from_ = from;
        to_ = to;
    }

    public static Result<Event, Error> of(String description, String from,
                                          String to) {
        return parse_date(from)
            .and_then(f -> parse_date(to).map(t -> new Pair<>(f, t)))
            .map(dates -> new Event(description, dates.left, dates.right));
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
        String from = opt.get().left.inner();
        String to = opt.get().right.inner();
        return Event.of(description, from, to).map(e -> e);
    }
}
