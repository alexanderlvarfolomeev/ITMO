package expression;

public class Const extends AbstractExpression {
    private Number value;

    public Const(double value) {
        this.value = value;
    }

    public double evaluate(double valueOfX) {
        return value.doubleValue();
    }

    public int evaluate(int valueOfX) {
        return value.intValue();
    }
}
