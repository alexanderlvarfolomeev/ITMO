package expression.exceptions;

public class OddOpeningBracketException extends ParsingException {
    public OddOpeningBracketException(int position) {
        super("There are odd opening brackets in the expression. Maybe bracket at position "
                + position + " is need to be deleted.");
    }

    public OddOpeningBracketException(String formula, int position) {
        super("There are odd opening brackets in the expression. Maybe bracket at position " +
                +(position + 1) + " is need to be deleted.", formula, position);
    }
}
