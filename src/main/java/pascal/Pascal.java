package pascal;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;
import pascal.command.Command;
import pascal.common.Pair;
import pascal.common.Str;
import pascal.printer.Printer;
import pascal.result.Error;
import pascal.result.Result;
import pascal.task.Deadline;
import pascal.task.Event;
import pascal.task.Task;
import pascal.task.TaskList;
import pascal.task.Todo;

class Pascal {
    private final Scanner scanner_;
    private final PrintStream writer_;
    private final Printer printer_;
    private final TaskList tasks_;
    private boolean exited_;

    Pascal(InputStream input, Printer printer, Optional<Path> data_path) {
        scanner_ = new Scanner(input);
        writer_ = printer.get_print_stream().orElse(System.err);
        printer_ = printer;
        tasks_ = data_path.map(path -> TaskList.read(path).get())
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
            return Result.Err(Error.other("Invalid command. Try again."));
        }
        exited_ |= opt.get().left == Command.Bye;
        Result<String, Error> result =
            handle_command(opt.get().left, opt.get().right);
        tasks_.write(Path.of("pascal.txt"));
        return result;
    }

    Result<String, Error> handle_command(Command command, Str input) {
        Optional<Integer> opt;
        Optional<Pair<Str, Str>> pair_str;
        Str arg;
        String description;
        Task task;
        switch (command) {
            case List:
                return Result.Ok(tasks_.list());
            case Mark:
                if ((opt = input.parse_int()).isEmpty()) {
                    return Result.Err(
                        Error.other("Invalid input. Expected an integer."));
                }
                task = tasks_.get_unchecked(opt.get() - 1);
                task.mark_as_done();
                return Result.Ok(String.format(
                    "Nice! I've marked this task as done:\n%s", task));
            case Unmark:
                if ((opt = input.parse_int()).isEmpty()) {
                    return Result.Err(
                        Error.other("Invalid input. Expected an integer."));
                }
                task = tasks_.get_unchecked(opt.get() - 1);
                task.mark_as_done();
                return Result.Ok(String.format(
                    "OK, I've marked this task as not done yet:\n%s", task));
            case Delete:
                if ((opt = input.parse_int()).isEmpty()) {
                    return Result.Err(
                        Error.other("Invalid input. Expected an integer."));
                }
                return Result.Ok(delete_task(opt.get()));

            case Todo:
                description = input.inner();
                return Result.Ok(add_task(new Todo(description)));
            case Deadline:
                if ((pair_str = input.split_once("/by")).isEmpty()) {
                    return Result.Err(
                        Error.other("Invalid input. Expected an integer."));
                }
                description = pair_str.get().left.trim_end().inner();
                String by = pair_str.get().right.trim_start().inner();
                return Deadline.of(description, by).map(d -> add_task(d));
            case Event:
                if ((pair_str = input.split_once("/from")).isEmpty()) {
                    return Result.Err(
                        Error.other("Invalid input. Expected a \"/from\"."));
                }
                description = pair_str.get().left.trim_end().inner();
                arg = pair_str.get().right.trim_start();

                if ((pair_str = arg.split_once("/to")).isEmpty()) {
                    return Result.Err(
                        Error.other("Invalid input. Expected a \"/to\"."));
                }
                String from = pair_str.get().left.trim_end().inner();
                String to = pair_str.get().right.trim_start().inner();

                return Event.of(description, from, to).map(e -> add_task(e));
            case Bye:
                return Result.Ok("Bye. Hope to see you again soon!");
        }
        return Result.Err(Error.other("Unhandled command!"));
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
