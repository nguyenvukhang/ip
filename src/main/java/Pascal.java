import java.io.PrintStream;
import java.util.Scanner;

public class Pascal {
    private static Scanner scanner = new Scanner(System.in);
    private static PrintStream writer = System.out;

    static void println(String format, Object... args) {
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

    static String prompt() {
        writer.print("> ");
        writer.flush();
        return scanner.nextLine();
    }

    public static void main(String[] args) {
        String greet = "Hello! I'm Pascal!\nWhat can I do for you?\n";
        println(greet);

        String user_input;
        while (!(user_input = prompt()).equals("bye")) {
            println("You said: \"%s\"", user_input);
        }
        println("Bye. Hope to see you again soon!");
    }
}
