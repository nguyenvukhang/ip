public class Error {
    protected final ErrorKind kind_;
    protected final String msg_;

    Error(ErrorKind kind, String message) {
        kind_ = kind;
        msg_ = message;
    }
}
