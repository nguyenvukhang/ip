package pascal.common;

/**
 * A Pair.
 * So that functions can return more than one thing.
 */
public class Pair<V0, V1> {
    public V0 left;
    public V1 right;

    /** Pair two items together. */
    public Pair(V0 v0, V1 v1) {
        this.left = v0;
        this.right = v1;
    }
}
