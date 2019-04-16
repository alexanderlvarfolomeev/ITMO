package expression.exceptions;

public class DivisionByZeroException extends ExpressionException {
    public DivisionByZeroException(Number firstArgument, Number secondArgument) {
        super("division by zero " + firstArgument + " / " + secondArgument);
    }
}
