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
    private final Scanner scanner;
    private final PrintStream writer;
    private final Printer printer;
    private final TaskList tasks;
    private boolean isExited;

    Pascal(InputStream input, Printer printer, Optional<Path> dataPath) {
        scanner = new Scanner(input);
        writer = printer.getPrintStream().orElse(System.err);
        this.printer = printer;
        tasks = dataPath.map(path -> TaskList.read(path).get())
                    .orElseGet(() -> new TaskList());
        isExited = false;
    }

    public boolean isExited() {
        return isExited;
    }

    private void println(String format, Object... args) {
        printer.println(format, args);
    }

    private Str prompt() {
        writer.print("> ");
        writer.flush();
        String response = scanner.nextLine();
        if (System.getenv("DEBUG") != null) {
            writer.println(response);
        }
        return new Str(response);
    }

    /**
     * Adds a task to the list.
     *
     * And then returns the output intended for the user.
     */
    private String addTask(Task task) {
        tasks.add(task);
        return String.format("added: %s\n%s", task, tasks.nowHave());
    }

    /**
     * Removes a task from the list.
     *
     * And then returns the output intended for the user.
     *
     * Assumes that `idx` points to a valid task.
     */
    private String deleteTask(int idx) {
        Task task = tasks.removeUnchecked(idx - 1);
        return String.format("Noted. I've removed this task:\n%s\n%s", task,
                             tasks.nowHave());
    }

    Result<String, Error> handleUserInput(String userInput) {
        Optional<Pair<Command, Str>> opt = Command.parse(new Str(userInput));
        if (opt.isEmpty()) {
            return Result.err(Error.other("Invalid command. Try again."));
        }
        isExited |= opt.get().left == Command.Bye;
        Result<String, Error> result =
            handleCommand(opt.get().left, opt.get().right);
        tasks.write(Path.of("pascal.txt"));
        return result;
    }

    Result<String, Error> handleCommand(Command command, Str input) {
        Optional<Integer> opt;
        Optional<Pair<Str, Str>> pairStr;
        Str arg;
        String description;
        Task task;
        switch (command) {
            case List:
                return Result.ok(tasks.list());
            case Mark:
                if ((opt = input.parseInt()).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected an integer."));
                }
                task = tasks.getUnchecked(opt.get() - 1);
                task.markAsDone();
                return Result.ok(String.format(
                    "Nice! I've marked this task as done:\n%s", task));
            case Unmark:
                if ((opt = input.parseInt()).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected an integer."));
                }
                task = tasks.getUnchecked(opt.get() - 1);
                task.markAsNotDone();
                return Result.ok(String.format(
                    "OK, I've marked this task as not done yet:\n%s", task));
            case Delete:
                if ((opt = input.parseInt()).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected an integer."));
                }
                return Result.ok(deleteTask(opt.get()));

            case Todo:
                description = input.inner();
                return Result.ok(addTask(new Todo(description)));
            case Deadline:
                if ((pairStr = input.splitOnce("/by")).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected an integer."));
                }
                description = pairStr.get().left.trimEnd().inner();
                String by = pairStr.get().right.trimStart().inner();
                return Deadline.of(description, by).map(d -> addTask(d));
            case Event:
                if ((pairStr = input.splitOnce("/from")).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected a \"/from\"."));
                }
                description = pairStr.get().left.trimEnd().inner();
                arg = pairStr.get().right.trimStart();

                if ((pairStr = arg.splitOnce("/to")).isEmpty()) {
                    return Result.err(
                        Error.other("Invalid input. Expected a \"/to\"."));
                }
                String from = pairStr.get().left.trimEnd().inner();
                String to = pairStr.get().right.trimStart().inner();

                return Event.of(description, from, to).map(e -> addTask(e));
            case Bye:
                return Result.ok("Bye. Hope to see you again soon!");
        }
        return Result.err(Error.other("Unhandled command!"));
    }

    public void run() {
        println("Hello! I'm Pascal!\nWhat can I do for you?\n");
        while (true) {
            Str userInput = prompt();
            Result<String, Error> result = handleUserInput(userInput.inner());
            if (result.isOk()) {
                println(result.get());
            } else if (result.isErr()) {
                println("%s", result.getErr());
            }
            if (isExited()) {
                return;
            }
        }
    }
}
