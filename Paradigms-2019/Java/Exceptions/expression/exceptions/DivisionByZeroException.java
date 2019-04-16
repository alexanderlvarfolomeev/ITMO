package expression.exceptions;

public class DivisionByZeroException extends ExpressionException {
    public DivisionByZeroException(int firstArgument, int secondArgument) {
        super("division by zero " + firstArgument + " / " + secondArgument);
    }
}
