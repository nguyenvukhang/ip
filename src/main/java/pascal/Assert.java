package pascal;

import java.io.PrintStream;
import pascal.printer.Color;
import pascal.result.Result;

class Assert {
    private static PrintStream ERR = System.err;

    private static void report(Object received, Object expected) {
        ERR.printf("%sAssertion Error.%s\n", Color.Red, Color.Reset);
        ERR.println("-----");
        ERR.println("Received:");
        ERR.println(received);
        ERR.println("Expected:");
        ERR.println(expected);
        ERR.println("-----");
        System.exit(1);
    }

    public static void eq(String received, String expected) {
        if (received.equals(expected))
            return;
        report(received, expected);
    }

    public static <T, E> void eq(Result<T, E> received, Result<T, E> expected) {
        if (received.isOk() && expected.isOk()) {
            if (received.get().equals(expected.get())) {
                return;
            }
        }
        if (received.isErr() && expected.isErr()) {
            if (received.getErr().equals(expected.getErr())) {
                return;
            }
        }
        report(received, expected);
    }
}
