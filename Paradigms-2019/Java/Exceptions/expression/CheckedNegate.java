package expression;

import expression.exceptions.ExpressionException;
import expression.exceptions.OverflowException;

public class CheckedNegate extends AbstractUnaryExpression {
    public CheckedNegate(TripleExpression argument) {
        super(argument);
    }

    public int calculate(int number) throws ExpressionException {
        if (number == Integer.MIN_VALUE) {
            throw new OverflowException("-" + number);
        }

        return -number;
    }
}
