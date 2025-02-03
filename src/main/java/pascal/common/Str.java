package pascal.common;

import java.util.Optional;

/**
 * A custom String class.
 * Implements all the forbidden string-parsing routines I want to only
 * ever see once.
 */
public final class Str {
    private String buf_;

    public Str(String value) {
        buf_ = value;
    }

    public String inner() {
        return buf_;
    }

    public Str trim() {
        return new Str(buf_.trim());
    }

    public Str trim_start() {
        return new Str(buf_.stripLeading());
    }

    public Str trim_end() {
        return new Str(buf_.stripTrailing());
    }

    public Optional<Str> strip_prefix(String prefix) {
        if (!buf_.startsWith(prefix)) {
            return Optional.empty();
        }
        return Optional.of(new Str(buf_.substring(prefix.length())));
    }

    public Str substr(int start, int end) {
        return new Str(buf_.substring(start, end));
    }

    public Str substr(int start) {
        return new Str(buf_.substring(start));
    }

    public Optional<Pair<Str, Str>> split_once(String delimiter) {
        int n = buf_.indexOf(delimiter);
        if (n == -1) {
            return Optional.empty();
        }
        return Optional.of(
            new Pair<Str, Str>(substr(0, n), substr(n + delimiter.length())));
        // if (!buf_.startsWith(prefix)) {
        //     return Optional.empty();
        // }
        // return Optional.of(new Str(buf_.substring(prefix.length())));
    }

    public Optional<Integer> parse_int() {
        try {
            return Optional.of(Integer.parseInt(buf_));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public boolean equals(String other) {
        return buf_.equals(other);
    }

    public boolean equals(Str other) {
        return buf_.equals(other.buf_);
    }

    @Override
    public String toString() {
        return buf_;
    }

    private void experiment() {
    }
}
