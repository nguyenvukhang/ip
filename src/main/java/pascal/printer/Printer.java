package pascal.printer;

import java.io.PrintStream;
import java.util.Optional;

/**
 * The abstract Printer.
 */
public interface Printer {
    /**
     * Obtains the underlying print stream.
     * Most of the time, it's stdout or stderr.
     */
    public Optional<PrintStream> getPrintStream();

    /**
     * THE printing function of `Printer`.
     * Like printf but with a newline at the end.
     */
    public void println(String format, Object... args);
}
