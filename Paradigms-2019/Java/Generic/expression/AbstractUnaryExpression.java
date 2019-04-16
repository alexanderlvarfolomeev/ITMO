package expression;

import expression.exceptions.ExpressionException;
import expression.exceptions.OverflowException;
import expression.operations.Operations;

public abstract class AbstractUnaryExpression<T extends Number & Comparable<T>> implements TripleExpression<T> {
    private final TripleExpression<T> argument;
    final Operations<T> operations;

    AbstractUnaryExpression(final TripleExpression<T> argument, final Operations<T> operations) {
        this.argument = argument;
        this.operations = operations;
    }

    public T evaluate(final T valueOfX, final T valueOfY, final T valueOfZ) throws ExpressionException {
        return calculate(argument.evaluate(valueOfX, valueOfY, valueOfZ));
    }

    abstract T calculate(final T number) throws OverflowException;

    private boolean checkForNull(T number) {
        return number == null;
    }
}
