package pascal.common;

/**
 * A Pair. So that functions can return more than one thing.
 */
public class Pair<A, B> {
    private A left;
    private B right;

    /** Pair two items together. */
    public Pair(A v0, B v1) {
        this.left = v0;
        this.right = v1;
    }

    /** Get the element on the left. */
    public A left() {
        return left;
    }

    /** Get the element on the right. */
    public B right() {
        return right;
    }
}
