package expression.exceptions;

public class InvalidOperationException extends ParsingException {
    public InvalidOperationException(String formula, int position, String token) {
        super("Expected operation but found unfamiliar token \"" + token + "\".", formula, position);
    }
}
