import java.util.Optional;

public final class Str {
    private String buf_;

    Str(String value) {
        buf_ = value;
    }

    String inner() {
        return buf_;
    }

    Str trim() {
        return new Str(buf_.trim());
    }

    Str trim_start() {
        return new Str(buf_.stripLeading());
    }

    Str trim_end() {
        return new Str(buf_.stripTrailing());
    }

    Optional<Str> strip_prefix(String prefix) {
        if (!buf_.startsWith(prefix)) {
            return Optional.empty();
        }
        return Optional.of(new Str(buf_.substring(prefix.length())));
    }

    Optional<Integer> parse_int() {
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

    private void experiment() {
    }
}
