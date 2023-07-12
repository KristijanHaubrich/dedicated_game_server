package org.game.structure;

public class GameVector3<T> {
    private final T x;
    private final T y;
    private final T z;

    public GameVector3(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static <T extends Number> GameVector3<T> of(final T x, final T y, final T z) {
        return new GameVector3<>(x, y, z);
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    public T getZ() {
        return z;
    }
}
