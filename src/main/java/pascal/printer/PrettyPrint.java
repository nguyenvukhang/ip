package pascal.printer;

import java.io.PrintStream;
import java.util.Optional;

/**
 * Fancy printer.
 */
public class PrettyPrint implements Printer {
    private final PrintStream writer_;
    private final char horz_, vert_, top_left_, top_right_, bottom_left_,
        bottom_right_;

    /** Construct a PrettyPrint. */
    public PrettyPrint(PrintStream writer) {
        writer_ = writer;
        horz_ = '─';
        vert_ = '│';
        top_left_ = '╭';
        top_right_ = '╮';
        bottom_left_ = '─';
        bottom_right_ = '╯';
    }

    /** Gets the maximum line length in a multiline `String`. */
    private int maxLen(String text) {
        int m = text.lines().map(String::length).max(Integer::compare).get();
        assert m <= 70 : "Keep hard-coded outputs to <= 70 chars per line.";
        return m;
    }

    /** Gets a horizontal ruler. */
    private String getHorizontal(int len) {
        return String.valueOf(horz_).repeat(len);
    }

    /** Gets PrettyPrint's underlying print stream. */
    public Optional<PrintStream> getPrintStream() {
        return Optional.of(writer_);
    }

    /**
     * THE pretty print function.
     * Like printf but with a newline at the end.
     */
    public void println(String format, Object... args) {
        String output = String.format(format, args);

        // Nothing to do here.
        if (output.length() == 0)
            return;

        // Unwrap safety guaranteed by the fact that output is non-empty.
        int max_line_len = maxLen(output);

        String rule = getHorizontal(max_line_len + 2);
        String top_rule = top_left_ + rule + top_right_;
        String bottom_rule = bottom_left_ + rule + bottom_right_;

        String line_fmt = "%-" + max_line_len + "s";

        // Begin the printing.
        writer_.println(top_rule);
        output.lines().forEach((line) -> {
            writer_.printf("%c %s", vert_, Color.Cyan);
            writer_.printf(line_fmt, line);
            writer_.printf("%s %c\n", Color.Reset, vert_);
        });
        writer_.println(bottom_rule);
    }
}
