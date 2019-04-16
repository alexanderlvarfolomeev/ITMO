package expression.exceptions;

public class OverflowException extends ExpressionException {
    public OverflowException(String expression) {
        super("overflow " + expression);
    }
}
