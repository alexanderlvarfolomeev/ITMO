package expression.parser;

import expression.*;

public class ExpressionParser implements Parser {
    enum Token {
        CONST, VARIABLE,
        NEGATE, NOT, COUNT,
        ADD, SUBTRACT, MULTIPLY, DIVIDE,
        AND, XOR, OR,
        LEFT_SHIFT, RIGHT_SHIFT,
        OPENING_BRACKET, CLOSING_BRACKET,
        END
    }

    private Token currentToken = Token.END;

    private int position = 0;

    private TripleExpression constOrVariable;

    private String formula;

    private void skipWhitespace() {
        while (position < formula.length() && Character.isWhitespace(formula.charAt(position))) {
            position++;
        }
    }

    private void nextToken() {
        skipWhitespace();
        if (position >= formula.length()) {
            currentToken = Token.END;
            return;
        }
        char symbol = formula.charAt(position);
        switch (symbol) {
            case '+':
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
                        currentToken = Token.NEGATE;
                }
                break;
            case '*':
                currentToken = Token.MULTIPLY;
                break;
            case '/':
                currentToken = Token.DIVIDE;
                break;
            case '(':
                currentToken = Token.OPENING_BRACKET;
                break;
            case ')':
                currentToken = Token.CLOSING_BRACKET;
                break;
            case '~':
                currentToken = Token.NOT;
                break;
            case '&':
                currentToken = Token.AND;
                break;
            case '^':
                currentToken = Token.XOR;
                break;
            case '|':
                currentToken = Token.OR;
                break;
            case '<':
                currentToken = Token.LEFT_SHIFT;
                position++;
                break;
            case '>':
                currentToken = Token.RIGHT_SHIFT;
                position++;
                break;
            case 'c':
                currentToken = Token.COUNT;
                position += 4;
                break;
            default:
                if (Character.isDigit(symbol)) {
                    currentToken = Token.CONST;
                    int firstPosition = position;
                    while (position < formula.length() && Character.isDigit(formula.charAt(position))) {
                        position++;
                    }
                    constOrVariable = new Const((int) Long.parseLong(formula.substring(firstPosition, position)));
                    position--;
                } else {
                    currentToken = Token.VARIABLE;
                    constOrVariable = new Variable(symbol);
                }
        }
        position++;
    }

    private TripleExpression highPriority() {
        nextToken();
        TripleExpression currentResult = null;
        switch (currentToken) {
            case CONST:
            case VARIABLE:
                currentResult = constOrVariable;
                nextToken();
                break;
            case OPENING_BRACKET:
                currentResult = shifts();
                nextToken();
                break;
            case COUNT:
                currentResult = new UnaryExpression(highPriority(), UnaryOperation.COUNT);
                break;
            case NEGATE:
                currentResult = new UnaryExpression(highPriority(), UnaryOperation.NEGATE);
                break;
            case NOT:
                currentResult = new UnaryExpression(highPriority(), UnaryOperation.NOT);
                break;
        }
        return currentResult;
    }

    private TripleExpression mediumPriority() {
        TripleExpression currentResult = highPriority();
        while (true) {
            switch (currentToken) {
                case MULTIPLY:
                    currentResult = new BinaryExpression(
                            currentResult, highPriority(), BinaryOperation.MULTIPLY);
                    break;
                case DIVIDE:
                    currentResult = new BinaryExpression(
                            currentResult, highPriority(), BinaryOperation.DIVIDE);
                    break;
                default:
                    return currentResult;
            }
        }
    }

    private TripleExpression lowPriority() {
        TripleExpression currentResult = mediumPriority();
        while (true) {
            switch (currentToken) {
                case ADD:
                    currentResult = new BinaryExpression(
                            currentResult, mediumPriority(), BinaryOperation.ADD);
                    break;
                case SUBTRACT:
                    currentResult = new BinaryExpression(
                            currentResult, mediumPriority(), BinaryOperation.SUBTRACT);
                    break;
                default:
                    return currentResult;
            }
        }
    }

    private TripleExpression and() {
        TripleExpression currentResult = lowPriority();
        while (true) {
            switch (currentToken) {
                case AND:
                    currentResult = new BinaryExpression(
                            currentResult, lowPriority(), BinaryOperation.AND);
                    break;
                default:
                    return currentResult;
            }
        }
    }

    private TripleExpression xor() {
        TripleExpression currentResult = and();
        while (true) {
            switch (currentToken) {
                case XOR:
                    currentResult = new BinaryExpression(
                            currentResult, and(), BinaryOperation.XOR);
                    break;
                default:
                    return currentResult;
            }
        }
    }

    private TripleExpression or() {
        TripleExpression currentResult = xor();
        while (true) {
            switch (currentToken) {
                case OR:
                    currentResult = new BinaryExpression(
                            currentResult, xor(), BinaryOperation.OR);
                    break;
                default:
                    return currentResult;
            }
        }
    }

    private TripleExpression shifts() {
        TripleExpression currentResult = or();
        while (true) {
            switch (currentToken) {
                case LEFT_SHIFT:
                    currentResult = new BinaryExpression(
                            currentResult, or(), BinaryOperation.LEFT_SHIFT);
                    break;
                case RIGHT_SHIFT:
                    currentResult = new BinaryExpression(
                            currentResult, or(), BinaryOperation.RIGHT_SHIFT);
                    break;
                default:
                    return currentResult;
            }
        }
    }

    public TripleExpression parse(String formula) {
        position = 0;
        this.formula = formula;
        return shifts();
    }
}
