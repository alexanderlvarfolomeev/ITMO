package expression;

import expression.exceptions.ExpressionException;

public abstract class AbstractUnaryExpression implements TripleExpression {
    private TripleExpression argument;

    public AbstractUnaryExpression(TripleExpression argument) {
        this.argument = argument;
    }

    public int evaluate(int valueOfX, int valueOfY, int valueOfZ) throws ExpressionException {
        return calculate(argument.evaluate(valueOfX, valueOfY, valueOfZ));
    }

    protected abstract int calculate(int number) throws ExpressionException;
}
