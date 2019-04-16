package expression;

public class Subtract extends AbstractBinaryExpression {

    public Subtract(AbstractExpression firstArgument, AbstractExpression secondArgument) {
        super(firstArgument, secondArgument);
    }

    public double evaluate(double valueOfX) {
        return firstArgument.evaluate(valueOfX) - secondArgument.evaluate(valueOfX);
    }

    public int evaluate(int valueOfX) {
        return firstArgument.evaluate(valueOfX) - secondArgument.evaluate(valueOfX);
    }
}
