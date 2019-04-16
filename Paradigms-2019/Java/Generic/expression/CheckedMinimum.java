package expression;

import expression.operations.Operations;

public class CheckedMinimum<T extends Number & Comparable<T>> extends AbstractBinaryExpression<T> {
    public CheckedMinimum(TripleExpression<T> firstArgument, TripleExpression<T> secondArgument, final Operations<T> operations) {
        super(firstArgument, secondArgument, operations);
    }

    public T calculate(T firstNumber, T secondNumber) {
        return operations.minimum(firstNumber, secondNumber);
    }
}