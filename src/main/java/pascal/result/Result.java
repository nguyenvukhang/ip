package pascal.result;

import java.util.Optional;
import java.util.function.Function;

/**
 * A Result.
 * Can be in only ONE of two states: Ok, or Err.
 * To replace Exceptions in the long run.
 */
public final class Result<T, E> {
    private Optional<T> value;
    private Optional<E> err;

    /** Construct a result. */
    private Result(Optional<T> value, Optional<E> err) {
        this.value = value;
        this.err = err;
    }

    /** Construct a result of the Ok variant. */
    public static <T, E> Result<T, E> ok(T value) {
        return new Result<>(Optional.of(value), Optional.empty());
    }

    /** Construct a result of the Err variant. */
    public static <T, E> Result<T, E> err(E error) {
        return new Result<>(Optional.empty(), Optional.of(error));
    }

    /** Checks if a result is of the Ok variant. */
    public boolean isOk() {
        return value.isPresent();
    }

    /** Checks if a result is of the Err variant. */
    public boolean isErr() {
        return err.isPresent();
    }

    /** Gets the value. Works only if the result is of the Ok variant. */
    public T get() {
        return value.get();
    }

    /** Gets the error. Works only if the result is of the Err variant. */
    public E getErr() {
        return err.get();
    }

    /** Converts the Result to another type. */
    public <U> Result<U, E> map(Function<? super T, ? extends U> f) {
        return new Result<>(value.map(f), err);
    }

    /** Converts the Result to another type. */
    public <U> Result<U, E> andThen(Function<? super T, Result<U, E>> f) {
        return isOk() ? f.apply(get()) : Result.err(getErr());
    }

    /** Print the result. */
    @Override
    public String toString() {
        if (isOk()) {
            return String.format("Ok(%s)", value.get());
        } else {
            return String.format("Err(%s)", err.get());
        }
    }
}
