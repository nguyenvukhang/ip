import java.io.PrintStream;

public class Pascal {
    static void println(String format, Object... args) {
        PrintStream writer = System.out;

        String output = String.format(format, args);

        // Nothing to do here.
        if (output.length() == 0)
            return;

        // Unwrap safety guaranteed by the fact that output is non-empty.
        int max_line_len =
            output.lines().map(String::length).max(Integer::compare).get();

        assert max_line_len <= 70
            : "Keep hard-coded outputs to <= 70 chars per line.";

        String rule = "─".repeat(max_line_len + 2);
        String fmt = "│ \033[36m%-" + max_line_len + "s\033[m │\n";

        writer.println('╭' + rule + '╮');
        output.lines().forEach((line) -> writer.printf(fmt, line));
        writer.println('─' + rule + '╯');
    }

    public static void main(String[] args) {
        String greet = "Hello! I'm Pascal!\nWhat can I do for you?\n";

        println(greet);
    }
}
