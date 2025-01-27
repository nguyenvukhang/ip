import command.Command;
import common.Pair;
import common.Str;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;
import printer.Printer;
import task.Task;
import task.TaskList;

public class Pascal {
    private final Scanner scanner_;
    private final PrintStream writer_;
    private final Printer printer_;
    private final TaskList tasks_;
    private boolean exited_;

    Pascal(InputStream input, Printer printer, Optional<Path> data_path) {
        scanner_ = new Scanner(input);
        writer_ = printer.get_print_stream().orElse(System.err);
        printer_ = printer;
        tasks_ = data_path.map(path -> TaskList.read(path))
                     .orElseGet(() -> new TaskList());
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
     * Adds a task to the list.
     *
     * And then returns the output intended for the user.
     */
    private String add_task(Task task) {
        tasks_.add(task);
        return String.format("added: %s\n%s", task, tasks_.now_have());
    }

    /**
     * Removes a task from the list.
     *
     * And then returns the output intended for the user.
     *
     * Assumes that `idx` points to a valid task.
     */
    private String delete_task(int idx) {
        Task task = tasks_.remove_unchecked(idx - 1);
        return String.format("Noted. I've removed this task:\n%s\n%s", task,
                             tasks_.now_have());
    }

    Result<String, Error> handle_cli_line(String user_input) {
        Optional<Pair<Command, Str>> opt = Command.parse(new Str(user_input));
        if (opt.isEmpty()) {
            return Result.err(Error.other("Invalid command. Try again."));
        }
        exited_ |= opt.get().v0 == Command.Bye;
        Result<String, Error> result =
            handle_command(opt.get().v0, opt.get().v1);
        tasks_.write(Path.of("pascal.txt"));
        return result;
    }

    Result<String, Error> handle_command(Command command, Str input) {
        Optional<Integer> opt;
        Optional<Pair<Str, Str>> pair_str;
        Str arg0, arg1, arg2;
        Task task;
        switch (command) {
            case List:
                return Result.ok(tasks_.list());
            case Mark:
                if ((opt = input.parse_int()).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected an integer."));
                }
                task = tasks_.get_unchecked(opt.get() - 1);
                task.mark_as_done();
                return Result.ok(String.format(
                    "Nice! I've marked this task as done:\n%s", task));
            case Unmark:
                if ((opt = input.parse_int()).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected an integer."));
                }
                task = tasks_.get_unchecked(opt.get() - 1);
                task.mark_as_done();
                return Result.ok(String.format(
                    "OK, I've marked this task as not done yet:\n%s", task));
            case Delete:
                if ((opt = input.parse_int()).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected an integer."));
                }
                return Result.ok(delete_task(opt.get()));
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
