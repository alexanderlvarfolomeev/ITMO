package expression.exceptions;

public class NotRealNumberException extends ExpressionException {
    public NotRealNumberException(String message) {
        super("Result is not a real number: " + message);
    }
}
