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

    private void print_list() {
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < task_count_; ++j) {
            sb.append(String.format("%d. %s", j + 1, tasks_[j]));
            if (j + 1 < task_count_) {
                sb.append('\n');
            }
        }
        printer_.println(sb.toString());
    }

    private void add_task(Task task) {
        tasks_[task_count_++] = task;
        String msg = String.format("added: %s", task);
        msg += "\n";
        String tasks = String.format(task_count_ == 1 ? "%d task" : "%d tasks",
                                     task_count_);
        msg += String.format("Now you have %s in the list.", tasks);
        println(msg);
    }

    void handle_cli_line(String user_input) {
        Optional<Pair<Command, Str>> opt = Command.parse(new Str(user_input));
        if (opt.isEmpty()) {
            println("Invalid command. Try again.");
            return;
        }
        handle_command(opt.get().v0, opt.get().v1);
        exited_ |= opt.get().v0 == Command.Bye;
    }

    void handle_command(Command command, Str input) {
        Optional<Integer> opt;
        Optional<Pair<Str, Str>> pair_str;
        Str arg0, arg1, arg2;
        Task task;
        switch (command) {
            case Mark:
                if ((opt = input.parse_int()).isEmpty()) {
                    println("Invalid input. Expected an integer.");
                    return;
                }
                task = tasks_[opt.get() - 1];
                task.mark_as_done();
                println("Nice! I've marked this task as done:\n%s", task);
                break;
            case Unmark:
                if ((opt = input.parse_int()).isEmpty()) {
                    println("Invalid input. Expected an integer.");
                    return;
                }
                task = tasks_[opt.get() - 1];
                task.mark_as_done();
                println("OK, I've marked this task as not done yet:\n%s", task);
                break;
            case List:
                print_list();
                break;
            case Todo:
                add_task(new task.Todo(input.inner()));
                break;
            case Deadline:
                if ((pair_str = input.split_once("/by")).isEmpty()) {
                    println("Invalid input. Expected a \"/by\".");
                    return;
                }
                arg0 = pair_str.get().v0.trim_end();
                arg1 = pair_str.get().v1.trim_start();
                add_task(new task.Deadline(arg0.inner(), arg1.inner()));
                break;
            case Event:
                if ((pair_str = input.split_once("/from")).isEmpty()) {
                    println("Invalid input. Expected a \"/from\".");
                    return;
                }
                arg0 = pair_str.get().v0.trim_end();
                arg1 = pair_str.get().v1.trim_start();

                if ((pair_str = arg1.split_once("/to")).isEmpty()) {
                    println("Invalid input. Expected a \"/to\".");
                    return;
                }
                arg1 = pair_str.get().v0.trim_end();
                arg2 = pair_str.get().v1.trim_start();

                add_task(
                    new task.Event(arg0.inner(), arg1.inner(), arg2.inner()));
                break;
            case Bye:
                println("Bye. Hope to see you again soon!");
        }
    }

    public void run() {
        println("Hello! I'm Pascal!\nWhat can I do for you?\n");
        while (true) {
            Str user_input = prompt();
            handle_cli_line(user_input.inner());
            if (is_exited()) {
                return;
            }
        }
    }
}
