package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ExpressionException;
import expression.exceptions.OverflowException;

public class CheckedDivide extends AbstractBinaryExpression {
    public CheckedDivide(TripleExpression firstArgument, TripleExpression secondArgument) {
        super(firstArgument, secondArgument);
    }

    public int calculate(int firstNumber, int secondNumber) throws ExpressionException {
        if (secondNumber == 0) {
            throw new DivisionByZeroException(firstNumber,secondNumber);
        }

        if (firstNumber == Integer.MIN_VALUE && secondNumber == -1) {
            throw new OverflowException(firstNumber + " / " + secondNumber);
        }

        return firstNumber / secondNumber;
    }
}
