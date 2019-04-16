package expression.exceptions;

public class OddClosingBracketException extends ParsingException{
    public OddClosingBracketException(int position) {
        super("There are odd closing brackets in the expression. Maybe bracket at position " +
                                + position + " is need to be deleted.");
    }

    public OddClosingBracketException(String formula, int position) {
        super("There are odd closing brackets in the expression. Maybe bracket at position " +
                + (position + 1) + " is need to be deleted.", formula, position);
    }
}
