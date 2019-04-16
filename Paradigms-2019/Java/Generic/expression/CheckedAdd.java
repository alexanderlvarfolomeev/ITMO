package expression;


import expression.exceptions.OverflowException;
import expression.operations.Operations;

public class CheckedAdd<T extends Number & Comparable<T>> extends AbstractBinaryExpression<T> {
    public CheckedAdd(TripleExpression<T> firstArgument, TripleExpression<T> secondArgument, final Operations<T> operations) {
        super(firstArgument, secondArgument, operations);
    }

    public T calculate(T firstNumber, T secondNumber) throws OverflowException {
        return operations.add(firstNumber, secondNumber);
    }
}
