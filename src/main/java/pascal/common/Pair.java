package pascal.common;

/**
 * A Pair.
 * So that functions can return more than one thing.
 */
public class Pair<A, B> {
    public A left;
    public B right;

    /** Pair two items together. */
    public Pair(A v0, B v1) {
        this.left = v0;
        this.right = v1;
    }
}
