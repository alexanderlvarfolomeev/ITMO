package expression;

public class Variable extends AbstractExpression {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public double evaluate(double valueOfX) {
        return valueOfX;
    }

    public int evaluate(int valueOfX) {
        return valueOfX;
    }
}
