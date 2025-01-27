import java.nio.file.Path;
import java.util.Optional;
import printer.Color;
import printer.PrettyPrint;
import printer.Tester;
import result.Error;
import result.Result;

class Test {
    private final Pascal pascal_;

    Test(Pascal pascal) {
        pascal_ = pascal;
    }

    static Result<String, Error> ok(String... output) {
        return Result.Ok(String.join("\n", output));
    }

    static String j(String... output) {
        return String.join("\n", output);
    }

    private void test(String input, Result<String, Error> output) {
        Assert.eq(pascal_.handle_cli_line(input), output);
    }

    static void test01(Pascal pascal) {
        Test t = new Test(pascal);

        t.test("hello", Result.Err(Error.other("Invalid command. Try again.")));

        t.test("todo read book",
               Result.Ok(j("added: [T][ ] read book",
                           "Now you have 1 task in the list.")));

        t.test("deadline return book /by June 6th",
               Result.Ok(j("added: [D][ ] return book (by: June 6th)",
                           "Now you have 2 tasks in the list.")));

        t.test("event project meeting /from Aug 6th 2pm /to 4pm",
               Result.Ok(j(
                   "added: [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)",
                   "Now you have 3 tasks in the list.")));

        t.test("todo join sports club",
               Result.Ok(j("added: [T][ ] join sports club",
                           "Now you have 4 tasks in the list.")));

        t.test("todo borrow book",
               Result.Ok(j("added: [T][ ] borrow book",
                           "Now you have 5 tasks in the list.")));

        t.test("mark 1", Result.Ok(j("Nice! I've marked this task as done:",
                                     "[T][X] read book")));

        t.test("mark 4", Result.Ok(j("Nice! I've marked this task as done:",
                                     "[T][X] join sports club")));

        t.test(
            "list",
            Result.Ok(
                j("1. [T][X] read book", "2. [D][ ] return book (by: June 6th)",
                  "3. [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)",
                  "4. [T][X] join sports club", "5. [T][ ] borrow book")));

        t.test("bye", Result.Ok("Bye. Hope to see you again soon!"));
    }

    /**
     * Test task deletion.
     */
    static void test02(Pascal pascal) {
        Test t = new Test(pascal);

        t.test("todo send blue",
               Result.Ok(j("added: [T][ ] send blue",
                           "Now you have 1 task in the list.")));

        t.test("todo send red",
               Result.Ok(j("added: [T][ ] send red",
                           "Now you have 2 tasks in the list.")));

        t.test("todo send green",
               Result.Ok(j("added: [T][ ] send green",
                           "Now you have 3 tasks in the list.")));

        t.test("todo send purple",
               Result.Ok(j("added: [T][ ] send purple",
                           "Now you have 4 tasks in the list.")));

        t.test("list",
               Result.Ok(j("1. [T][ ] send blue", "2. [T][ ] send red",
                           "3. [T][ ] send green", "4. [T][ ] send purple")));

        t.test("delete 2",
               Result.Ok(j("Noted. I've removed this task:\n[T][ ] send red",
                           "Now you have 3 tasks in the list.")));

        t.test("bye", Result.Ok("Bye. Hope to see you again soon!"));
    }
}

class Main {
    static boolean is_test(String[] args) {
        return args.length >= 1 && args[0].equals("test");
    }

    static void run_tests() {
        Tester t = new Tester();
        Test.test01(new Pascal(System.in, t, Optional.empty()));
        // Test.test02(new Pascal(System.in, t));
    }

    public static void main(String[] args) {
        if (is_test(args)) {
            run_tests();
            System.out.printf("%sAll tests passed!%s\n", Color.Green,
                              Color.Reset);
            return;
        }
        // Printer printer = is_test ? new Tester() : ;
        new Pascal(System.in, new PrettyPrint(System.out),
                   Optional.of(Path.of("pascal.txt")))
            .run();
    }
}
