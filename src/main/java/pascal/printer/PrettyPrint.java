package pascal.printer;

import java.io.PrintStream;
import java.util.Optional;

/**
 * Fancy printer.
 */
public class PrettyPrint implements Printer {
    private final PrintStream writer;
    private final char horizontal, vertical, topLeft, topRight, bottomLeft,
        bottomRight;

    /** Construct a PrettyPrint. */
    public PrettyPrint(PrintStream writer) {
        this.writer = writer;
        horizontal = '─';
        vertical = '│';
        topLeft = '╭';
        topRight = '╮';
        bottomLeft = '─';
        bottomRight = '╯';
    }

    /** Gets the maximum line length in a multiline `String`. */
    private int maxLen(String text) {
        int m = text.lines().map(String::length).max(Integer::compare).get();
        assert m <= 70 : "Keep hard-coded outputs to <= 70 chars per line.";
        return m;
    }

    /** Gets a horizontal ruler. */
    private String getHorizontal(int len) {
        return String.valueOf(horizontal).repeat(len);
    }

    /** Gets PrettyPrint's underlying print stream. */
    public Optional<PrintStream> getPrintStream() {
        return Optional.of(writer);
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
        int maxLineLen = maxLen(output);

        String rule = getHorizontal(maxLineLen + 2);
        String topRule = topLeft + rule + topRight;
        String bottomRule = bottomLeft + rule + bottomRight;

        String lineFmt = "%-" + maxLineLen + "s";

        // Begin the printing.
        writer.println(topRule);
        output.lines().forEach((line) -> {
            writer.printf("%c %s", vertical, Color.Cyan);
            writer.printf(lineFmt, line);
            writer.printf("%s %c\n", Color.Reset, vertical);
        });
        writer.println(bottomRule);
    }
}
