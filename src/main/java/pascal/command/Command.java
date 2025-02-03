package pascal.command;

import java.util.List;
import java.util.Optional;
import pascal.common.Pair;
import pascal.common.Str;

public enum Command {
    /** List the tasks in memory. */
    List,

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
            Optional<Str> z = input.strip_prefix(p.left).map(Str::trim_start);
            if (z.isPresent()) {
                return Optional.of(new Pair<>(p.right, z.get()));
            }
        }
        return Optional.empty();
    }
}
