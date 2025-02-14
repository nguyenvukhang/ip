package pascal;

import java.nio.file.Path;
import java.util.Optional;

import pascal.command.Command;
import pascal.common.Pair;
import pascal.common.Str;
import pascal.result.Error;
import pascal.result.Result;
import pascal.task.Deadline;
import pascal.task.Event;
import pascal.task.Task;
import pascal.task.TaskList;
import pascal.task.Todo;

/**
 * The Pascal ChatBot.
 */
public class Pascal {
    /** Internal list of tasks. */
    private final TaskList tasks;

    /** If the server has exited. */
    private boolean isExited;

    /** Construct with a data source. */
    public Pascal(Optional<Path> dataPath) {
        tasks = dataPath.map(path -> TaskList.read(path).get())
                    .orElseGet(() -> new TaskList());
        isExited = false;
    }

    /** Construct with default data source. */
    public Pascal() {
        this(Optional.of(Path.of("pascal.txt")));
    }

    public boolean isExited() {
        return isExited;
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

    /** Handles one line of user input. */
    public Result<String, Error> handleUserInput(String userInput) {
        Optional<Pair<Command, Str>> opt = Command.parse(new Str(userInput));
        if (opt.isEmpty()) {
            return Result.err(Error.other("Invalid command. Try again."));
        }
        isExited |= opt.get().left() == Command.Bye;
        Result<String, Error> result =
            handleCommand(opt.get().left(), opt.get().right());
        tasks.write(Path.of("pascal.txt"));
        return result;
    }

    Result<String, Error> handleCommand(Command command, Str input) {
        Optional<Integer> opt;
        Optional<Pair<Str, Str>> pairStr;
        Str arg;
        String description;
        String query;
        Task task;
        switch (command) {
        case List:
            return Result.ok(tasks.listPretty());
        case Find:
            query = input.inner();
            return Result.ok(tasks.findPretty(query));
        case Remind:
            return Result.ok(tasks.upcomingWeekPretty());
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
            description = pairStr.get().left().trimEnd().inner();
            String by = pairStr.get().right().trimStart().inner();
            return Deadline.of(description, by).map(d -> addTask(d));
        case Event:
            if ((pairStr = input.splitOnce("/from")).isEmpty()) {
                return Result.err(
                    Error.other("Invalid input. Expected a \"/from\"."));
            }
            description = pairStr.get().left().trimEnd().inner();
            arg = pairStr.get().right().trimStart();

            if ((pairStr = arg.splitOnce("/to")).isEmpty()) {
                return Result.err(
                    Error.other("Invalid input. Expected a \"/to\"."));
            }
            String from = pairStr.get().left().trimEnd().inner();
            String to = pairStr.get().right().trimStart().inner();

            return Event.of(description, from, to).map(e -> addTask(e));
        case Bye:
            return Result.ok("Bye. Hope to see you again soon!");
        default:
            return Result.err(Error.other("Unhandled command!"));
        }
    }
}
