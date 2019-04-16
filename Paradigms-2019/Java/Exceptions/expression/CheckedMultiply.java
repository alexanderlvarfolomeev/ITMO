package expression;

import expression.exceptions.ExpressionException;
import expression.exceptions.OverflowException;

public class CheckedMultiply extends AbstractBinaryExpression {
    public CheckedMultiply(TripleExpression firstArgument, TripleExpression secondArgument) {
        super(firstArgument, secondArgument);
    }

    public int calculate(int firstNumber, int secondNumber) throws ExpressionException {
        if (firstNumber > 0 && secondNumber > 0 && firstNumber > Integer.MAX_VALUE / secondNumber
                || firstNumber < 0 && secondNumber < 0 && firstNumber < Integer.MAX_VALUE / secondNumber
                || firstNumber < 0 && secondNumber > 0 && firstNumber < Integer.MIN_VALUE / secondNumber
                || firstNumber > 0 && secondNumber < 0 && secondNumber < Integer.MIN_VALUE / firstNumber) {
            throw new OverflowException(firstNumber + " * " + secondNumber);
        }

        return firstNumber * secondNumber;
    }
}
