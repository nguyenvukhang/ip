import java.util.Optional;

public final class Result<T, E> {
    private Optional<T> value_;
    private Optional<E> err_;
}
