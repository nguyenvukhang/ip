package result;

public class Error {
    protected final ErrorKind kind_;
    protected final String msg_;

    Error(ErrorKind kind, String message) {
        kind_ = kind;
        msg_ = message;
    }

    public static Error other(String format, Object... args) {
        return new Error(ErrorKind.Other, String.format(format, args));
    }

    public boolean equals(Object other) {
        if (!(other instanceof Error))
            return false;

        Error rhs = (Error)other;
        if (kind_ != rhs.kind_) {
            return false;
        }
        return msg_.equals(rhs.msg_);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", kind_, msg_);
    }
}
