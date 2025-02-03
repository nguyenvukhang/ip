package pascal;

import java.io.PrintStream;
import pascal.printer.Color;
import pascal.result.Result;

class Assert {
    private static PrintStream err = System.err;

    private static void report(Object received, Object expected) {
        err.printf("%sAssertion Error.%s\n", Color.Red, Color.Reset);
        err.println("-----");
        err.println("Received:");
        err.println(received);
        err.println("Expected:");
        err.println(expected);
        err.println("-----");
        System.exit(1);
    }

    public static void eq(String received, String expected) {
        if (received.equals(expected))
            return;
        report(received, expected);
    }

    public static <T, E> void eq(Result<T, E> received, Result<T, E> expected) {
        if (received.is_ok() && expected.is_ok()) {
            if (received.get().equals(expected.get())) {
                return;
            }
        }
        if (received.is_err() && expected.is_err()) {
            if (received.get_err().equals(expected.get_err())) {
                return;
            }
        }
        report(received, expected);
    }
}
