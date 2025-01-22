public class Pascal {
    static void println(String format, Object... args) {
        System.out.printf(format, args);
        System.out.println();
    }

    public static void main(String[] args) {
        String logo =
            ""
            + "____________________________________________________________\n"
            + "  Hello! I'm Pascal!\n"
            + "  What can I do for you?\n"
            + "____________________________________________________________\n"
            + "  Bye. Hope to see you again soon!\n"
            + "____________________________________________________________";
        println(logo);
    }
}
