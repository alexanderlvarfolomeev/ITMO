package expression;


import expression.operations.Operations;

public class CheckedMaximum<T extends Number & Comparable<T>> extends AbstractBinaryExpression<T> {
    public CheckedMaximum(TripleExpression<T> firstArgument, TripleExpression<T> secondArgument, final Operations<T> operations) {
        super(firstArgument, secondArgument, operations);
    }

    public T calculate(T firstNumber, T secondNumber) {
        return operations.maximum(firstNumber, secondNumber);
    }
}
