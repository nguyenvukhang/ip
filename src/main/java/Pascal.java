import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import task.Task;
import task.Todo;

enum Command {
    /** List the tasks in memory. */
    List,

    /** Adds a todo. */
    Todo,

    /** Marks a task as complete. */
    Mark,

    /** Marks a task as incomplete. */
    Unmark,

    /** Adds a deadline. */
    Deadline,

    /** Adds an event. */
    Event,

    /** Quits the session. */
    Bye;

    private static Pair<String, Command> pair(String s, Command c) {
        return new Pair<String, Command>(s, c);
    }

    private static List<Pair<String, Command>> command_map() {
        return java.util.List.of(       //
            pair("list", List),         //
            pair("mark", Mark),         //
            pair("unmark", Unmark),     //
            pair("todo", Todo),         //
            pair("deadline", Deadline), //
            pair("event", Event),       //
            pair("bye", Bye)            //
        );
    }

    /**
     * Parses a command out of the first word of `input`, and then returns the
     * command, along with the remnants of the input.
     */
    public static Optional<Pair<Command, Str>> parse(Str input) {
        for (Pair<String, Command> p : command_map()) {
            Optional<Str> z = input.strip_prefix(p.v0).map(Str::trim_start);
            if (z.isPresent()) {
                return Optional.of(new Pair<>(p.v1, z.get()));
            }
        }
        return Optional.empty();
    }
}

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

    private Str prompt() {
        writer_.print("> ");
        writer_.flush();
        String response = scanner_.nextLine();
        if (System.getenv("DEBUG") != null) {
            writer_.println(response);
        }
        return new Str(response);
    }

    private void print_list() {
        String buf = "";
        for (int j = 0; j < task_count_; ++j) {
            Task task = tasks_[j];
            buf += String.format("%d. %s", j + 1, task);
            if (j < tasks_.length) {
                buf += '\n';
            }
        }
        println(buf);
    }

    private void add_task(Task task) {
        tasks_[task_count_++] = task;
        String msg = String.format("added: %s", task);
        msg += "\n";
        msg += String.format("Now you have %d tasks in the list.", task_count_);
        println(msg);
    }

    /**
     * Handles a `Command` and then returns a boolean. Boolean is true implies
     * we want to continue reading commands.
     */
    boolean handle_command(Command command, Str input) {
        Optional<Integer> opt;
        switch (command) {
            case Mark:
                if ((opt = input.parse_int()).isEmpty()) {
                    println("Invalid input. Expected an integer.");
                    return true;
                }
                tasks_[opt.get() - 1].mark_as_done();
                break;
            case Unmark:
                if ((opt = input.parse_int()).isEmpty()) {
                    println("Invalid input. Expected an integer.");
                    return true;
                }
                tasks_[opt.get() - 1].mark_as_not_done();
                break;
            case List:
                print_list();
                break;
            case Todo:
                add_task(new task.Todo(input.inner()));
                break;
            case Deadline:
                Optional<Pair<Str, Str>> pair_str = input.split_once("/by");
                if (pair_str.isEmpty()) {
                    println("Invalid input. Expected a \"/by\".");
                    return true;
                }
                Str left = pair_str.get().v0.trim_end();
                Str right = pair_str.get().v1.trim_start();
                add_task(new task.Deadline(left.inner(), right.inner()));
                break;
            case Event:
                add_task(new task.Event(input.inner()));
                break;
            case Bye:
                println("Bye. Hope to see you again soon!");
                return false;
        }
        return true;
    }

    void event_loop() {
        while (true) {
            Str user_input = prompt();
            Optional<Pair<Command, Str>> opt = Command.parse(user_input);
            if (opt.isEmpty()) {
                println("Invalid command. Try again.");
                continue;
            }
            if (!handle_command(opt.get().v0, opt.get().v1))
                break;
        }
    }

    public void run() {
        println("Hello! I'm Pascal!\nWhat can I do for you?\n");
        event_loop();
    }
}
