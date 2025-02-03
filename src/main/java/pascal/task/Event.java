package pascal.task;

import java.time.LocalDate;
import java.util.Optional;
import pascal.common.Pair;
import pascal.common.Str;
import pascal.result.Error;
import pascal.result.Result;

/**
 * An Event.
 * A task that has a start date and an end date.
 */
public class Event extends Task {
    protected LocalDate fromDate, toDate;

    /** Empty constructor for inner use. */
    public Event() {
        super("");
    }

    /** Create a Event Task. */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        fromDate = from;
        toDate = to;
    }

    /** Parse a Event Task from strings. */
    public static Result<Event, Error> of(String description, String from,
                                          String to) {
        return parseDate(from)
            .andThen(f -> parseDate(to).map(t -> new Pair<>(f, t)))
            .map(dates -> new Event(description, dates.left, dates.right));
    }

    /** Enum icon of a Event Task */
    public char getEnumIcon() {
        return 'E';
    }

    /** Description of a Event Task */
    public String getDescription() {
        return String.format("%s (from: %s to: %s)", description, fromDate, toDate);
    }

    /** Serialize a Event Task to save it to the filesystem. */
    public String serialize() {
        return String.format("%s::%s::%s", description, fromDate, toDate);
    }

    /** Deserialize a Event Task from a String. */
    public Result<Task, Error> deserialize(String text) {
        Str x = new Str(text);
        Optional<Pair<Str, Str>> opt;
        opt = x.splitOnce("::");
        if (opt.isEmpty()) {
            return Result.err(Error.other("Error in parsing an `Event`."));
        }

        String description = opt.get().left.inner();
        opt = opt.get().right.splitOnce("::");
        if (opt.isEmpty()) {
            return Result.err(Error.other("Error in parsing an `Event`."));
        }
        String from = opt.get().left.inner();
        String to = opt.get().right.inner();
        return Event.of(description, from, to).map(e -> e);
    }
}
