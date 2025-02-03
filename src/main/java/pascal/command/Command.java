package pascal.command;

import java.util.List;
import java.util.Optional;
import pascal.common.Pair;
import pascal.common.Str;

/**
 * CLI Command.
 *
 * All the possible sub-actions to take.
 */
public enum Command {
    /** List the tasks in memory. */
    List,

    /** Find a task by substring search. */
    Find,

    /** Marks a task as complete. */
    Mark,

    /** Marks a task as incomplete. */
    Unmark,

    /** Deletes an event. */
    Delete,

    /** Adds a todo. */
    Todo,

    /** Adds a deadline. */
    Deadline,

    /** Adds an event. */
    Event,

    /** Quits the session. */
    Bye;

    private static Pair<String, Command> pair(String s, Command c) {
        return new Pair<String, Command>(s, c);
    }

    private static List<Pair<String, Command>> COMMAND_MAP =
        java.util.List.of(              //
            pair("list", List),         //
            pair("find", Find),         //
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
        for (Pair<String, Command> p : COMMAND_MAP) {
            Optional<Str> z = input.stripPrefix(p.left).map(Str::trimStart);
            if (z.isPresent()) {
                return Optional.of(new Pair<>(p.right, z.get()));
            }
        }
        return Optional.empty();
    }
}
