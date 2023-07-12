package org.game.structure;

public class GameVector2<T> {
    private final T x;
    private final T y;
    public GameVector2(T x, T y) {
        this.x = x;
        this.y = y;
    }
    public static <T extends Number> GameVector2<T> of(final T x, final T y) {
        return new GameVector2<>(x, y);
    }
    public static GameVector2<Float> of(final String x, final String y) {
        return new GameVector2<>(Float.valueOf(x), Float.valueOf(y));
    }
    public T getX() {
        return x;
    }
    public T getY() {
        return y;
    }

}
