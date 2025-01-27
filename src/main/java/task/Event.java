package task;

import common.Pair;
import common.Str;

public class Event extends Task {
    protected String from_, to_;

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

    public Task deserialize(String text) {
        Str x = new Str(text);
        Pair<Str, Str> pair = x.split_once("::").get();
        String description = pair.v0.inner();
        pair = pair.v1.split_once("::").get();
        return new Event(description, pair.v0.inner(), pair.v1.inner());
    }
}
