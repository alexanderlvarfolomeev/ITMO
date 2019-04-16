package expression;

import expression.exceptions.ExpressionException;

public abstract class AbstractBinaryExpression implements TripleExpression {
    private TripleExpression firstArgument;
    private TripleExpression secondArgument;

    public AbstractBinaryExpression(TripleExpression firstArgument, TripleExpression secondArgument) {
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
    }

    public int evaluate(int valueOfX, int valueOfY, int valueOfZ) throws ExpressionException {
        return calculate(firstArgument.evaluate(valueOfX, valueOfY, valueOfZ),
                secondArgument.evaluate(valueOfX, valueOfY, valueOfZ));
    }

    protected abstract int calculate(int firstNumber, int secondNumber) throws ExpressionException;
}
