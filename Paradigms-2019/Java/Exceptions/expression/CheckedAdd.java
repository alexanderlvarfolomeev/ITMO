package expression;

import expression.AbstractBinaryExpression;
import expression.TripleExpression;
import expression.exceptions.ExpressionException;
import expression.exceptions.OverflowException;

public class CheckedAdd extends AbstractBinaryExpression {
    public CheckedAdd(TripleExpression firstArgument, TripleExpression secondArgument) {
        super(firstArgument, secondArgument);
    }

    public int calculate(int firstNumber, int secondNumber) throws ExpressionException {
        if (secondNumber > 0 && Integer.MAX_VALUE - secondNumber < firstNumber
                || secondNumber < 0 && Integer.MIN_VALUE - secondNumber > firstNumber) {
            throw new OverflowException(firstNumber + " + " + secondNumber);
        }

        return firstNumber + secondNumber;
    }
}
