package pascal;

import java.util.Optional;

import pascal.command.Command;
import pascal.common.Pair;
import pascal.common.Str;
import pascal.result.Error;
import pascal.result.Result;

class Parser {
    /** Handles one line of user input. */
    public static Result<Pair<Command, Str>, Error> parse(String userInput) {
        Optional<Pair<Command, Str>> opt = Command.parse(new Str(userInput));
        if (opt.isEmpty()) {
            return Result.err(Error.other("Invalid command. Try again."));
        }
        return Result.ok(opt.get());
    }
}
