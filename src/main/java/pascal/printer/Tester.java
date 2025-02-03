package pascal.printer;

import java.io.PrintStream;
import java.util.Optional;

public class Tester implements Printer {
    private String buffer_;

    public Tester() {
        buffer_ = "";
    }

    public Optional<PrintStream> get_print_stream() {
        return Optional.empty();
    }

    public void println(String format, Object... args) {
        buffer_ = String.format(format, args);
    }

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

    public void assert_prev_eq(String... expected) {
        assert_prev_eq(String.join("\n", expected));
    }

    public void assert_prev_eq(String expected) {
        if (expected.equals(buffer_)) {
            return;
        }
        report(expected, buffer_);
    }
}
