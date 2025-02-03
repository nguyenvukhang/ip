package pascal.common;

import java.util.Optional;

/**
 * A custom String class.
 * Implements all the forbidden string-parsing routines I want to only
 * ever see once.
 */
public final class Str {
    private String buf_;

    /** Create a `Str` from a `String`. */
    public Str(String value) {
        buf_ = value;
    }

    /** Return the inner value as a `String`. */
    public String inner() {
        return buf_;
    }

    /** Trims surrouding whitespace, and return the newly formed `Str`. */
    public Str trim() {
        return new Str(buf_.trim());
    }

    /** Trims starting whitespace, and return the newly formed `Str`. */
    public Str trim_start() {
        return new Str(buf_.stripLeading());
    }

    /** Trims ending whitespace, and return the newly formed `Str`. */
    public Str trim_end() {
        return new Str(buf_.stripTrailing());
    }

    /**
     * Strips a set prefix, and return the newly formed `Str`.
     * If the prefix is not found, an empty Optional is returned.
     * */
    public Optional<Str> strip_prefix(String prefix) {
        if (!buf_.startsWith(prefix)) {
            return Optional.empty();
        }
        return Optional.of(new Str(buf_.substring(prefix.length())));
    }

    /**
     * Obtains a substring.
     * Exact same API as Java Standard Library.
     */
    public Str substr(int start, int end) {
        return new Str(buf_.substring(start, end));
    }

    /**
     * Obtains a substring.
     * Exact same API as Java Standard Library.
     */
    public Str substr(int start) {
        return new Str(buf_.substring(start));
    }

    /**
     * Splits a string once by a delimiter.
     * Returns an empty optional if the delimiter is not found.
     */
    public Optional<Pair<Str, Str>> split_once(String delimiter) {
        int n = buf_.indexOf(delimiter);
        if (n == -1) {
            return Optional.empty();
        }
        return Optional.of(
            new Pair<Str, Str>(substr(0, n), substr(n + delimiter.length())));
    }

    /**
     * Parses an integer from the contents.
     * Returns an empty optional if the contents aren't parseable.
     */
    public Optional<Integer> parse_int() {
        try {
            return Optional.of(Integer.parseInt(buf_));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /** Compares to a `String`. */
    public boolean equals(String other) {
        return buf_.equals(other);
    }

    /** Compares to another `Str`. */
    public boolean equals(Str other) {
        return buf_.equals(other.buf_);
    }

    /** Display the `Str` as normal. */
    @Override
    public String toString() {
        return buf_;
    }
}
