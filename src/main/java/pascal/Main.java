package pascal;

import java.nio.file.Path;
import java.util.Optional;
import pascal.printer.Color;
import pascal.printer.PrettyPrint;
import pascal.printer.Tester;
import pascal.result.Error;
import pascal.result.Result;

class Test {
    private final Pascal pascal;

    Test(Pascal pascal) {
        this.pascal = pascal;
    }

    static Result<String, Error> ok(String... output) {
        return Result.ok(String.join("\n", output));
    }

    static String j(String... output) {
        return String.join("\n", output);
    }

    private void test(String input, Result<String, Error> output) {
        Assert.eq(pascal.handleUserInput(input), output);
    }

    static void test01(Pascal pascal) {
        Test t = new Test(pascal);

        t.test("hello", Result.err(Error.other("Invalid command. Try again.")));

        t.test("todo read book",
               Result.ok(j("added: [T][ ] read book",
                           "Now you have 1 task in the list.")));

        t.test("deadline return book /by 2025-05-29",
               Result.ok(j("added: [D][ ] return book (by: 2025-05-29)",
                           "Now you have 2 tasks in the list.")));

        t.test("event project meeting /from 2025-05-29 /to 2025-06-15",
               Result.ok(j("added: [E][ ] project meeting (from: 2025-05-29 "
                               + "to: 2025-06-15)",
                           "Now you have 3 tasks in the list.")));

        t.test("todo join sports club",
               Result.ok(j("added: [T][ ] join sports club",
                           "Now you have 4 tasks in the list.")));

        t.test("todo borrow book",
               Result.ok(j("added: [T][ ] borrow book",
                           "Now you have 5 tasks in the list.")));

        t.test("mark 1", Result.ok(j("Nice! I've marked this task as done:",
                                     "[T][X] read book")));

        t.test("mark 4", Result.ok(j("Nice! I've marked this task as done:",
                                     "[T][X] join sports club")));

        t.test(
            "list",
            Result.ok(j(
                "1. [T][X] read book", "2. [D][ ] return book (by: 2025-05-29)",
                "3. [E][ ] project meeting (from: 2025-05-29 to: 2025-06-15)",
                "4. [T][X] join sports club", "5. [T][ ] borrow book")));

        t.test("bye", Result.ok("Bye. Hope to see you again soon!"));
    }

    /**
     * Test task deletion.
     */
    static void test02(Pascal pascal) {
        Test t = new Test(pascal);

        t.test("todo send blue",
               Result.ok(j("added: [T][ ] send blue",
                           "Now you have 1 task in the list.")));

        t.test("todo send red",
               Result.ok(j("added: [T][ ] send red",
                           "Now you have 2 tasks in the list.")));

        t.test("todo send green",
               Result.ok(j("added: [T][ ] send green",
                           "Now you have 3 tasks in the list.")));

        t.test("todo send purple",
               Result.ok(j("added: [T][ ] send purple",
                           "Now you have 4 tasks in the list.")));

        t.test("list",
               Result.ok(j("1. [T][ ] send blue", "2. [T][ ] send red",
                           "3. [T][ ] send green", "4. [T][ ] send purple")));

        t.test("delete 2",
               Result.ok(j("Noted. I've removed this task:\n[T][ ] send red",
                           "Now you have 3 tasks in the list.")));

        t.test("bye", Result.ok("Bye. Hope to see you again soon!"));
    }

    /**
     * Test task search.
     */
    static void test03(Pascal pascal) {
        Test t = new Test(pascal);

        t.test("todo send blue",
               Result.ok(j("added: [T][ ] send blue",
                           "Now you have 1 task in the list.")));

        t.test("todo send red",
               Result.ok(j("added: [T][ ] send red",
                           "Now you have 2 tasks in the list.")));

        t.test("todo send green",
               Result.ok(j("added: [T][ ] send green",
                           "Now you have 3 tasks in the list.")));

        t.test("todo send purple",
               Result.ok(j("added: [T][ ] send purple",
                           "Now you have 4 tasks in the list.")));

        t.test("list",
               Result.ok(j("1. [T][ ] send blue", "2. [T][ ] send red",
                           "3. [T][ ] send green", "4. [T][ ] send purple")));

        t.test("find blue", Result.ok(j("1. [T][ ] send blue")));

        t.test("find green", Result.ok(j("1. [T][ ] send green")));

        t.test("bye", Result.ok("Bye. Hope to see you again soon!"));
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

    /** Test runner. */
    static void runTests() {
        Tester t = new Tester();
        Test.test01(new Pascal(System.in, t, Optional.empty()));
        Test.test02(new Pascal(System.in, t, Optional.empty()));
        Test.test03(new Pascal(System.in, t, Optional.empty()));
    }

    /** The entrypoint. */
    public static void main(String[] args) {
        if (isTest(args)) {
            runTests();
            System.out.printf("%sAll tests passed!%s\n", Color.Green,
                              Color.Reset);
            return;
        }
        // Printer printer = isTest ? new Tester() : ;
        new Pascal(System.in, new PrettyPrint(System.out),
                   Optional.of(Path.of("pascal.txt")))
            .run();
    }
}
