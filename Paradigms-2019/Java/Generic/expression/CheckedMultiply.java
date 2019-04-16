package expression;

import expression.exceptions.OverflowException;
import expression.operations.Operations;

public class CheckedMultiply<T extends Number & Comparable<T>> extends AbstractBinaryExpression<T> {
    public CheckedMultiply(TripleExpression<T> firstArgument, TripleExpression<T> secondArgument, final Operations<T> operations) {
        super(firstArgument, secondArgument, operations);
    }

    public T calculate(T firstNumber, T secondNumber) throws OverflowException {
        return operations.multiply(firstNumber, secondNumber);
    }
}
