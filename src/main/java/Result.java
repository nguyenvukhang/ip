import java.util.Optional;

public final class Result<T, E> {
    private Optional<T> value_;
    private Optional<E> err_;

    private Result(Optional<T> value, Optional<E> error) {
        value_ = value;
        err_ = error;
    }

    public static <T, E> Result<T, E> of(T value) {
        return new Result<>(Optional.of(value), Optional.empty());
    }

    public static <T, E> Result<T, E> err(E error) {
        return new Result<>(Optional.empty(), Optional.of(error));
    }
}
