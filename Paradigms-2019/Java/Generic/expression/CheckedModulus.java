package expression;

import expression.exceptions.DivisionByZeroException;
import expression.operations.Operations;

public class CheckedModulus<T extends Number & Comparable<T>> extends AbstractBinaryExpression<T>   {
    public CheckedModulus(TripleExpression<T> firstArgument, TripleExpression<T> secondArgument, final Operations<T> operations) {
        super(firstArgument, secondArgument, operations);
    }

    public T calculate(T firstNumber, T secondNumber) throws DivisionByZeroException {
        return operations.modulus(firstNumber, secondNumber);
    }
}
