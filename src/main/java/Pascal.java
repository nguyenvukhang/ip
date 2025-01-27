import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import printer.Printer;
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

    /** Deletes an event. */
    Delete,

    /** Quits the session. */
    Bye;

    private static Pair<String, Command> pair(String s, Command c) {
        return new Pair<String, Command>(s, c);
    }

    private static List<Pair<String, Command>> command_map =
        java.util.List.of(              //
            pair("list", List),         //
            pair("mark", Mark),         //
            pair("unmark", Unmark),     //
            pair("todo", Todo),         //
            pair("deadline", Deadline), //
            pair("event", Event),       //
            pair("delete", Delete),     //
            pair("bye", Bye)            //
        );

    /**
     * Parses a command out of the first word of `input`, and then returns the
     * command, along with the remnants of the input.
     */
    public static Optional<Pair<Command, Str>> parse(Str input) {
        for (Pair<String, Command> p : command_map) {
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
    private final Printer printer_;
    private final Task[] tasks_;
    private int task_count_;
    private boolean exited_;

    Pascal(InputStream input, Printer printer) {
        scanner_ = new Scanner(input);
        writer_ = printer.get_print_stream().orElse(System.err);
        printer_ = printer;
        tasks_ = new Task[100];
        task_count_ = 0;
        exited_ = false;
    }

    public boolean is_exited() {
        return exited_;
    }

    private void println(String format, Object... args) {
        printer_.println(format, args);
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

    /**
     * Returns the output intended for the user as a `String`.
     */
    private String print_list() {
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < task_count_; ++j) {
            sb.append(String.format("%d. %s", j + 1, tasks_[j]));
            if (j + 1 < task_count_)
                sb.append('\n');
        }
        return sb.toString();
    }

    private String now_have(int n) {
        String tasks = String.format(n == 1 ? "%d task" : "%d tasks", n);
        return String.format("Now you have %s in the list.", tasks);
    }

    /**
     * Adds a task to the list.
     *
     * And then returns the output intended for the user.
     */
    private String add_task(Task task) {
        tasks_[task_count_++] = task;
        return String.format("added: %s\n%s", task, now_have(task_count_));
    }

    /**
     * Removes a task from the list.
     *
     * And then returns the output intended for the user.
     *
     * Assumes that `idx` points to a valid task.
     */
    private String delete_task(int idx) {
        Task task = tasks_[idx - 1];
        String message = String.format("Noted. I've removed this task:\n%s\n%s",
                                       task, now_have(task_count_ - 1));
        for (int i = idx; i <= task_count_; ++i) {
            tasks_[i - 1] = tasks_[i];
        }
        tasks_[--task_count_] = null;
        return message;
    }

    Result<String, Error> handle_cli_line(String user_input) {
        Optional<Pair<Command, Str>> opt = Command.parse(new Str(user_input));
        if (opt.isEmpty()) {
            return Result.err(Error.other("Invalid command. Try again."));
        }
        exited_ |= opt.get().v0 == Command.Bye;
        return handle_command(opt.get().v0, opt.get().v1);
    }

    Result<String, Error> handle_command(Command command, Str input) {
        Optional<Integer> opt;
        Optional<Pair<Str, Str>> pair_str;
        Str arg0, arg1, arg2;
        Task task;
        switch (command) {
            case Mark:
                if ((opt = input.parse_int()).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected an integer."));
                }
                task = tasks_[opt.get() - 1];
                task.mark_as_done();
                return Result.ok(String.format(
                    "Nice! I've marked this task as done:\n%s", task));
            case Unmark:
                if ((opt = input.parse_int()).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected an integer."));
                }
                task = tasks_[opt.get() - 1];
                task.mark_as_done();
                return Result.ok(String.format(
                    "OK, I've marked this task as not done yet:\n%s", task));
            case List:
                return Result.ok(print_list());
            case Todo:
                return Result.ok(add_task(new task.Todo(input.inner())));
            case Deadline:
                if ((pair_str = input.split_once("/by")).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected an integer."));
                }
                arg0 = pair_str.get().v0.trim_end();
                arg1 = pair_str.get().v1.trim_start();
                return Result.ok(
                    add_task(new task.Deadline(arg0.inner(), arg1.inner())));
            case Event:
                if ((pair_str = input.split_once("/from")).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected a \"/from\"."));
                }
                arg0 = pair_str.get().v0.trim_end();
                arg1 = pair_str.get().v1.trim_start();

                if ((pair_str = arg1.split_once("/to")).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected a \"/to\"."));
                }
                arg1 = pair_str.get().v0.trim_end();
                arg2 = pair_str.get().v1.trim_start();

                return Result.ok(add_task(
                    new task.Event(arg0.inner(), arg1.inner(), arg2.inner())));
            case Delete:
                if ((opt = input.parse_int()).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected an integer."));
                }
                return Result.ok(delete_task(opt.get()));
            case Bye:
                return Result.ok("Bye. Hope to see you again soon!");
        }
        return Result.err(Error.other("Unhandled command!"));
    }

    public void run() {
        println("Hello! I'm Pascal!\nWhat can I do for you?\n");
        while (true) {
            Str user_input = prompt();
            Result<String, Error> result = handle_cli_line(user_input.inner());
            if (result.is_ok()) {
                println(result.get());
            } else if (result.is_err()) {
                println("%s", result.get_err());
            }
            if (is_exited()) {
                return;
            }
        }
    }
}
