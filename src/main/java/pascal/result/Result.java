package pascal.result;

import java.util.Optional;
import java.util.function.Function;

/**
 * A Result.
 * Can be in only ONE of two states: Ok, or Err.
 * To replace Exceptions in the long run.
 */
public final class Result<T, E> {
    private Optional<T> value_;
    private Optional<E> err_;

    /** Construct a result. */
    private Result(Optional<T> value, Optional<E> error) {
        value_ = value;
        err_ = error;
    }

    /** Construct a result of the Ok variant. */
    public static <T, E> Result<T, E> Ok(T value) {
        return new Result<>(Optional.of(value), Optional.empty());
    }

    /** Construct a result of the Err variant. */
    public static <T, E> Result<T, E> Err(E error) {
        return new Result<>(Optional.empty(), Optional.of(error));
    }

    /** Checks if a result is of the Ok variant. */
    public boolean is_ok() {
        return value_.isPresent();
    }

    /** Checks if a result is of the Err variant. */
    public boolean is_err() {
        return err_.isPresent();
    }

    /** Gets the value. Works only if the result is of the Ok variant. */
    public T get() {
        return value_.get();
    }

    /** Gets the error. Works only if the result is of the Err variant. */
    public E get_err() {
        return err_.get();
    }

    /** Converts the Result to another type. */
    public <U> Result<U, E> map(Function<? super T, ? extends U> f) {
        return new Result<>(value_.map(f), err_);
    }

    /** Converts the Result to another type. */
    public <U> Result<U, E> and_then(Function<? super T, Result<U, E>> f) {
        return is_ok() ? f.apply(get()) : Result.Err(get_err());
    }

    /** Print the result. */
    @Override
    public String toString() {
        if (is_ok()) {
            return String.format("Ok(%s)", value_.get());
        } else {
            return String.format("Err(%s)", err_.get());
        }
    }
}
