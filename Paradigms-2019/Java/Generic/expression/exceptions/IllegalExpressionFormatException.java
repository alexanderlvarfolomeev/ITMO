package expression.exceptions;

public class IllegalExpressionFormatException extends ParsingException {
    public IllegalExpressionFormatException(String message, int position) {
        super(message + position + '.');
    }

    public IllegalExpressionFormatException(String formula, String message, int position) {
        super(message + (position + 1) + '.', formula, position );
    }
}
