package expression;

public class Variable<T extends Number & Comparable<T>> implements TripleExpression<T>{
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public T evaluate(T valueOfX, T valueOfY, T valueOfZ) {
        switch (name) {
            case "x":
                return valueOfX;
            case "y":
                return valueOfY;
            case "z":
                return valueOfZ;
            default:
                return null;
        }
    }
}
