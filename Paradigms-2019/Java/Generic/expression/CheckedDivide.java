package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;
import expression.operations.Operations;

public class CheckedDivide<T extends Number & Comparable<T>> extends AbstractBinaryExpression<T> {
    public CheckedDivide(TripleExpression<T> firstArgument, TripleExpression<T> secondArgument, final Operations<T> operations) {
        super(firstArgument, secondArgument, operations);
    }

    public T calculate(T firstNumber, T secondNumber) throws DivisionByZeroException, OverflowException {

        return operations.divide(firstNumber, secondNumber);
    }
}
