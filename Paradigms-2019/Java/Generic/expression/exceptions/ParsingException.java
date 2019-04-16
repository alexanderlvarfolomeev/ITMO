package expression.exceptions;

class ParsingException extends ParserException {
    ParsingException(String message) {
        super(message);
    }

    ParsingException(String message, String formula, int position) {
        this(message + '\n' + formula + '\n' + buildPointer(position));
    }

    static private String buildPointer(int position) {
        StringBuilder pointer = new StringBuilder();
        for (int i = 0; i < position; i++) {
            pointer.append(' ');
        }
        pointer.append('^');
        return pointer.toString();
    }
}
