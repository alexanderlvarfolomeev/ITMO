package expression.parser;

import expression.exceptions.*;

import java.util.Stack;

class Tokenizer {
    private Stack<Integer> bracketPositions = new Stack<>();

    private Token currentToken;

    private int position;

    private String formula;

    private String token;

    Tokenizer(String formula) {
        position = 0;
        bracketPositions.clear();
        currentToken = Token.BEGIN;
        this.formula = formula;
    }

    private void skipWhitespace() {
        while (position < formula.length() && Character.isWhitespace(formula.charAt(position))) {
            position++;
        }
    }

    private boolean checkEndOfFormula() {
        return position >= formula.length();
    }

    private void checkOperation(int shift) throws IllegalExpressionFormatException {
        switch (currentToken) {
            case ADD:
            case DIVIDE:
            case MULTIPLY:
            case SUBTRACT:
            case MODULUS:
            case SQUARE_ROOT:
            case HIGH:
            case LOW:
            case MINIMUM:
            case MAXIMUM:
            case BEGIN:
            case OPENING_BRACKET:
                throw new IllegalExpressionFormatException(formula,
                        "Awaiting for argument at position ",
                        position - shift);
        }
    }

    private void checkArguments(int shift) throws IllegalExpressionFormatException {
        switch (currentToken) {
            case CLOSING_BRACKET:
            case CONST:
            case VARIABLE:
                throw new IllegalExpressionFormatException("Awaiting for operation at position ", position - shift + 1);
        }
    }

    private String getNumber(int firstPosition) {
        while (position < formula.length() && Character.isDigit(formula.charAt(position))) {
            position++;
        }
        String result = formula.substring(firstPosition, position);
        position--;
        return result;
    }

    private String getToken(int firstPosition) {
        while (position < formula.length() && Character.isLetterOrDigit(formula.charAt(position))) {
            position++;
        }
        String result = formula.substring(firstPosition, position);
        position--;
        return result;
    }

    void nextToken() throws ParserException {
        skipWhitespace();
        if (checkEndOfFormula()) {
            checkOperation(0);
            currentToken = Token.END;
            return;
        }
        char symbol = formula.charAt(position);
        switch (symbol) {
            case '+':
                checkOperation(0);
                currentToken = Token.ADD;
                break;
            case '-':
                switch (currentToken) {
                    case CLOSING_BRACKET:
                    case VARIABLE:
                    case CONST:
                        currentToken = Token.SUBTRACT;
                        break;
                    default:
                        position++;
                        skipWhitespace();
                        if (checkEndOfFormula()) {
                            throw new IllegalExpressionFormatException("Awaiting for next argument at position ", position);
                        }
                        if (Character.isDigit(formula.charAt(position))) {
                            currentToken = Token.CONST;
                            token = "-" + getNumber(position);
                        } else {
                            if (Character.isLetter(formula.charAt(position))
                                    || formula.charAt(position) == '('
                                    || formula.charAt(position) == '-') {
                                currentToken = Token.NEGATE;
                                position--;
                            } else {
                                throw new InvalidOperationException(formula, position, getToken(position));
                            }
                        }
                }
                break;
            case '*':
                checkOperation(0);
                currentToken = Token.MULTIPLY;
                break;
            case '/':
                checkOperation(0);
                currentToken = Token.DIVIDE;
                break;
            case '(':
                checkArguments(1);
                bracketPositions.push(position);
                currentToken = Token.OPENING_BRACKET;
                break;
            case ')':
                if (bracketPositions.empty()) {
                    throw new OddClosingBracketException(formula, position);
                }
                bracketPositions.pop();
                checkOperation(0);
                currentToken = Token.CLOSING_BRACKET;
                break;
            default:
                if (Character.isDigit(symbol)) {
                    checkArguments(1);
                    token = getNumber(position);
                    currentToken = Token.CONST;
                } else {
                    token = getToken(position);
                    switch (token) {
                        case "x":
                        case "y":
                        case "z":
                            checkArguments(token.length());
                            currentToken = Token.VARIABLE;
                            break;
                        case "abs":
                            checkArguments(token.length());
                            currentToken = Token.MODULUS;
                            break;
                        case "sqrt":
                            checkArguments(token.length());
                            currentToken = Token.SQUARE_ROOT;
                            break;
                        case "high":
                            checkArguments(token.length());
                            currentToken = Token.HIGH;
                            break;
                        case "low":
                            checkArguments(token.length());
                            currentToken = Token.LOW;
                            break;
                        case "min":
                            checkOperation(token.length());
                            currentToken = Token.MINIMUM;
                            break;
                        case "max":
                            checkOperation(token.length());
                            currentToken = Token.MAXIMUM;
                            break;
                        case "":
                            throw new UnexpectedSymbolException(symbol, position + 2);
                        default:
                            throw new InvalidOperationException(formula, position - token.length() + 1, token);
                    }
                }
        }
        position++;
    }

    Token getCurrentToken() {
        return currentToken;
    }
    String getToken() {
        return token;
    }

    int checkForCorrectBracketsSequence() {
        if (!bracketPositions.empty()) {
            return bracketPositions.pop();
        } else {
            return -1;
        }
    }
}
