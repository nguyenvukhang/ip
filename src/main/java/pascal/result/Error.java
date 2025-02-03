package pascal.result;

/**
 * An Error.
 * Contains the kind of error along with the error message.
 */
public class Error {
    protected final ErrorKind kind;
    protected final String message;

    /** Construct an Error. */
    Error(ErrorKind kind, String message) {
        this.kind = kind;
        this.message = message;
    }

    /** Gets the Error's type/kind. */
    public ErrorKind getKind() {
        return kind;
    }

    /** Gets the Error's underlying error message. */
    public String getMessage() {
        return message;
    }

    /** Convenience routine to make an Other variant of Errors. */
    public static Error other(String format, Object... args) {
        return new Error(ErrorKind.Other, String.format(format, args));
    }

    /** Equality comparison of errors. */
    public boolean equals(Object other) {
        if (!(other instanceof Error))
            return false;

        Error rhs = (Error)other;
        if (kind != rhs.kind) {
            return false;
        }
        return message.equals(rhs.message);
    }

    /** Displaying errors. */
    @Override
    public String toString() {
        return String.format("%s: %s", kind, message);
    }
}
