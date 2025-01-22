import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Pascal {
    private final Scanner scanner_;
    private final PrintStream writer_;
    private final String[] tasks_;

    Pascal(InputStream input, PrintStream output) {
        scanner_ = new Scanner(input);
        writer_ = output;
        tasks_ = new String[100];
    }

    private void println(String format, Object... args) {
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

        writer_.println('╭' + rule + '╮');
        output.lines().forEach((line) -> writer_.printf(fmt, line));
        writer_.println('─' + rule + '╯');
    }

    private String prompt() {
        writer_.print("> ");
        writer_.flush();
        return scanner_.nextLine();
    }

    private void handle_input(String input) {
    }

    public void run() {
        String greet = "Hello! I'm Pascal!\nWhat can I do for you?\n";
        println(greet);

        String user_input;
        while (!(user_input = prompt()).equals("bye")) {
            handle_input(user_input);
        }
        println("Bye. Hope to see you again soon!");
    }
}
