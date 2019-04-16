package expression.exceptions;

public class UnexpectedSymbolException extends ParsingException {
    public UnexpectedSymbolException(char symbol, int position) {
        super("unexpected symbol \'" + symbol + "\' found at position " + position + '.');
    }
}
