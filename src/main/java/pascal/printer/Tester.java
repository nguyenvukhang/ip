package pascal.printer;

import java.io.PrintStream;
import java.util.Optional;

/**
 * A dummy Printer.
 */
public class Tester implements Printer {
    private String buffer_;

    /** Construct a new Tester. */
    public Tester() {
        buffer_ = "";
    }

    /** Gets the Tester's print stream: Nothing. */
    public Optional<PrintStream> get_print_stream() {
        return Optional.empty();
    }

    /** Print stuff, but to the inner buffer. */
    public void println(String format, Object... args) {
        buffer_ = String.format(format, args);
    }

    /**
     * Report an assertion error.
     * Exits immediately after.
     */
    private void report(String expected, String received) {
        System.err.printf("%sAssertion Error.%s\n", Color.Red, Color.Reset);
        System.err.println("-----");
        System.err.print("Expected: ");
        System.err.println(expected);
        System.err.print("Received: ");
        System.err.println(received);
        System.err.println("-----");
        System.exit(1);
    }

    /**
     * Assert equality on the last thing printed.
     * Arguments will be joined by the newline character.
     */
    public void assert_prev_eq(String... expected) {
        assert_prev_eq(String.join("\n", expected));
    }

    /** Assert equality on the last thing printed. */
    public void assert_prev_eq(String expected) {
        if (expected.equals(buffer_)) {
            return;
        }
        report(expected, buffer_);
    }
}
