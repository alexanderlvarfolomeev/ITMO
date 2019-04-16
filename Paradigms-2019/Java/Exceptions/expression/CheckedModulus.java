package expression;

import expression.exceptions.ExpressionException;
import expression.exceptions.OverflowException;

public class CheckedModulus extends AbstractUnaryExpression {
    public CheckedModulus(TripleExpression argument) {
        super(argument);
    }

    public int calculate(int number) throws ExpressionException {
        if (number == Integer.MIN_VALUE) {
            throw new OverflowException("|" + number + "|");
        }

        return number >= 0 ? number : -number;
    }
}
