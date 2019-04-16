package expression;

import expression.exceptions.OverflowException;
import expression.operations.Operations;

public class CheckedAbsolute<T extends Number & Comparable<T>> extends AbstractUnaryExpression<T> {
    public CheckedAbsolute(TripleExpression<T> argument, final Operations<T> operations) {
        super(argument, operations);
    }

    public T calculate(T number) throws OverflowException {
        return operations.absolute(number);
    }
}
