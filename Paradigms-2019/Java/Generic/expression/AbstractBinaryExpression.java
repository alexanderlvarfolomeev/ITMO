package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ExpressionException;
import expression.exceptions.OverflowException;
import expression.operations.Operations;

public abstract class AbstractBinaryExpression<T extends Number & Comparable<T>> implements TripleExpression<T> {
    private final TripleExpression<T> firstArgument;
    private final TripleExpression<T> secondArgument;
    final Operations<T> operations;

    AbstractBinaryExpression(final TripleExpression<T> firstArgument,
                             final TripleExpression<T> secondArgument,
                             final Operations<T> operations) {
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
        this.operations = operations;
    }

    public T evaluate(final T valueOfX, final T valueOfY, final T valueOfZ) throws ExpressionException {
        return calculate(firstArgument.evaluate(valueOfX, valueOfY, valueOfZ),
                secondArgument.evaluate(valueOfX, valueOfY, valueOfZ));
    }

    abstract T calculate(final T firstNumber, final T secondNumber) throws OverflowException, DivisionByZeroException;

    private boolean checkForNull(T firstNumber, T secondNumber) {
        return firstNumber == null || secondNumber == null;
    }
}
