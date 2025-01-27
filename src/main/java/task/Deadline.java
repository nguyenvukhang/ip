package task;

import common.Pair;
import common.Str;
import result.Error;
import result.Result;

public class Deadline extends Task {
    protected String by_;

    public Deadline(String description, String by) {
        super(description);
        by_ = by;
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
        Pair<Str, Str> pair = x.split_once("::").get();
        return Result.Ok(new Deadline(pair.v0.inner(), pair.v1.inner()));
    }
}
