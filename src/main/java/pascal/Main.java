package pascal;

// clang-format off
import javafx.application.Application;
import pascal.printer.Tester;
import pascal.result.Error;
import pascal.result.Result;
import pascal.ui.App;
// clang-format on

class Test {
    private final Pascal pascal;

    Test(Pascal pascal) {
        this.pascal = pascal;
    }

    static Result<String, Error> ok(String... output) {
        return Result.ok(String.join("\n", output));
    }

    static String join(String... output) {
        return String.join("\n", output);
    }

    private void test(String input, Result<String, Error> output) {
        Assert.eq(pascal.handleUserInput(input), output);
    }
}

/**
 * The entrypoint.
 */
public class Main {
    /** Quick and dirty check if we should run tests. */
    static boolean isTest(String[] args) {
        return args.length >= 1 && args[0].equals("test");
    }

    /** Quick and dirty check if we should just quit. */
    static boolean isQuickQuit(String[] args) {
        return args.length >= 1 && args[0].equals("quit");
    }

    /** Quick and dirty check if we should run the GUI. */
    static boolean isGui(String[] args) {
        return args.length >= 1 && args[0].equals("gui");
    }

    /** Test runner. */
    static void runTests() {
        Tester t = new Tester();
    }

    /** GUI runner. */
    static void runGui(String[] args) {
        Application.launch(App.class, args);
    }

    /** The entrypoint. */
    public static void main(String[] args) {
        runGui(args);
    }
}
