package expression;

import expression.exceptions.ExpressionException;
import expression.exceptions.NotRealNumberException;

public class CheckedSquareRoot extends AbstractUnaryExpression {
    public CheckedSquareRoot(TripleExpression argument) {
        super(argument);
    }

    public int calculate(int number) throws ExpressionException {
        if (number < 0) {
            throw new NotRealNumberException("√" + number);
        }
        int result = 0;
        while (result * result <= number) {
            result++;
        }
        return result - 1;
    }
}
