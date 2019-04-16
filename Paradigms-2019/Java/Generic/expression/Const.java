package expression;

public class Const<T extends Number & Comparable<T>> implements TripleExpression<T>{
    private final T value;

    public Const(T value) {
        this.value = value;
    }

    public T evaluate(T valueOfX, T valueOfY, T valueOfZ) {
        return value;
    }
}
