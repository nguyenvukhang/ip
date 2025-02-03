package pascal.printer;

import java.io.PrintStream;
import java.util.Optional;

public interface Printer {
    public Optional<PrintStream> get_print_stream();
    public void println(String format, Object... args);
}
