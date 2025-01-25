import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Pascal {
    private final Scanner scanner_;
    private final PrintStream writer_;
    private final Task[] tasks_;
    private int task_count_;

    Pascal(InputStream input, PrintStream output) {
        scanner_ = new Scanner(input);
        writer_ = output;
        tasks_ = new Task[100];
        task_count_ = 0;
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

    private void print_list() {
        String buf = "";
        for (int j = 0; j < task_count_; ++j) {
            Task task = tasks_[j];
            buf += String.format("%d.[%s] %s", j + 1, task.get_status_icon(),
                                 task);
            if (j < tasks_.length) {
                buf += '\n';
            }
        }
        println(buf);
    }

    private void add_task(Task task) {
        tasks_[task_count_++] = task;
        println("added: %s", task);
    }

    private void handle_input(String input) {
        if (input.equals("list")) {
            print_list();
            return;
        }
        if (input.startsWith("mark")) {
            String num_str = input.replaceFirst("^mark", "").trim();
            int idx = Integer.parseInt(num_str) - 1;
            Task task = tasks_[idx];
            task.mark_as_done();
            println("Nice! I've marked this task as done:\n  [%s] %s",
                    task.get_status_icon(), task);
            return;
        }
        if (input.startsWith("unmark")) {
            String num_str = input.replaceFirst("^unmark", "").trim();
            int idx = Integer.parseInt(num_str) - 1;
            tasks_[idx].mark_as_not_done();
            return;
        }

        add_task(new Task(input));
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
