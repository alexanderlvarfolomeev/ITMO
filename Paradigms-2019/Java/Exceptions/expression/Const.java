package expression;

public class Const implements TripleExpression{
    private final int value;

    public Const(int value) {
        this.value = value;
    }

    public int evaluate(int valueOfX, int valueOfY, int valueOfZ) {
        return value;
    }
}
