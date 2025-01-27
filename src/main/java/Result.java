import java.util.Optional;

public final class Result<T, E> {
    private Optional<T> value_;
    private Optional<E> err_;

    private Result(Optional<T> value, Optional<E> error) {
        value_ = value;
        err_ = error;
    }

    public static <T, E> Result<T, E> ok(T value) {
        return new Result<>(Optional.of(value), Optional.empty());
    }

    public static <T, E> Result<T, E> err(E error) {
        return new Result<>(Optional.empty(), Optional.of(error));
    }

    public boolean is_ok() {
        return value_.isPresent();
    }

    public boolean is_err() {
        return err_.isPresent();
    }

    public T get() {
        return value_.get();
    }

    public E get_err() {
        return err_.get();
    }

    @Override
    public String toString() {
        if (is_ok()) {
            return String.format("Ok(%s)", value_.get());
        } else {
            return String.format("Err(%s)", err_.get());
        }
    }
}
