package pascal.printer;

/**
 * Colors. In ANSI.
 * To bring the CLI to life.
 */
public enum Color {
    Red,
    Green,
    Yellow,
    Blue,
    Purple,
    Cyan,
    Gray,
    Reset;

    @Override
    public String toString() {
        switch (this) {
        case Red:
            return "\033[31m";
        case Green:
            return "\033[32m";
        case Yellow:
            return "\033[33m";
        case Blue:
            return "\033[34m";
        case Purple:
            return "\033[35m";
        case Cyan:
            return "\033[36m";
        case Gray:
            return "\033[37m";
        case Reset:
            return "\033[m";
        default:
            return "\033[m";
        }
    }
}
