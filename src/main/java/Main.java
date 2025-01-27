import printer.Color;
import printer.PrettyPrint;
import printer.Tester;

class Test {
    static void test01(Tester t, Pascal p) {
        Result<Unit, Error> result;

        Assert.eq(p.handle_cli_line("hello"),
                  Result.err(Error.other("Invalid command. Try again.")));

        p.handle_cli_line("todo read book");
        t.assert_prev_eq("added: [T][ ] read book",
                         "Now you have 1 task in the list.");

        p.handle_cli_line("deadline return book /by June 6th");
        t.assert_prev_eq("added: [D][ ] return book (by: June 6th)",
                         "Now you have 2 tasks in the list.");

        p.handle_cli_line("event project meeting /from Aug 6th 2pm /to 4pm");
        t.assert_prev_eq(
            "added: [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)",
            "Now you have 3 tasks in the list.");

        p.handle_cli_line("todo join sports club");
        t.assert_prev_eq("added: [T][ ] join sports club",
                         "Now you have 4 tasks in the list.");

        p.handle_cli_line("todo borrow book");
        t.assert_prev_eq("added: [T][ ] borrow book",
                         "Now you have 5 tasks in the list.");

        p.handle_cli_line("mark 1");
        t.assert_prev_eq("Nice! I've marked this task as done:",
                         "[T][X] read book");

        p.handle_cli_line("mark 4");
        t.assert_prev_eq("Nice! I've marked this task as done:",
                         "[T][X] join sports club");

        p.handle_cli_line("list");
        t.assert_prev_eq(
            "1. [T][X] read book", "2. [D][ ] return book (by: June 6th)",
            "3. [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)",
            "4. [T][X] join sports club", "5. [T][ ] borrow book");

        p.handle_cli_line("bye");
        t.assert_prev_eq("Bye. Hope to see you again soon!");
    }
}

class Main {
    static boolean is_test(String[] args) {
        return args.length >= 1 && args[0].equals("test");
    }

    static void run_tests() {
        Tester t = new Tester();
        Pascal p = new Pascal(System.in, t);

        Test.test01(t, p);
    }

    public static void main(String[] args) {
        if (is_test(args)) {
            run_tests();
            System.out.printf("%sAll tests passed!%s\n", Color.Green,
                              Color.Reset);
            return;
        }
        // Printer printer = is_test ? new Tester() : ;
        new Pascal(System.in, new PrettyPrint(System.out)).run();
    }
}
