package pascal.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import pascal.result.Error;
import pascal.result.Result;

public abstract class Task {
    protected String description_;
    protected boolean is_done_;

    protected static DateTimeFormatter DT_FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected static LocalDate EMPTY_DATE = LocalDate.of(1, 1, 1);

    public Task(String description) {
        description_ = description;
        is_done_ = false;
    }

    public void mark_as_done() {
        is_done_ = true;
    }

    public void mark_as_not_done() {
        is_done_ = false;
    }

    public char get_status_icon() {
        return is_done_ ? 'X' : ' '; // mark done task with X
    }

    protected static Result<LocalDate, Error> parse_date(String text) {
        try {
            return Result.Ok(LocalDate.parse(text, DT_FMT));
        } catch (DateTimeParseException e) {
            return Result.Err(Error.other("Error parsing datetime: %s", e));
        }
    }

    abstract public char get_enum_icon();

    abstract public String get_description();

    abstract public String serialize();

    abstract public Result<Task, Error> deserialize(String text);

    @Override
    public String toString() {
        return String.format("[%s][%s] %s", get_enum_icon(), get_status_icon(),
                             get_description());
    }
}
