package expression.parser;

import expression.*;
import expression.exceptions.*;

public class ExpressionParser implements Parser {

    private Tokenizer tokenizer;

    private TripleExpression highPriorityOperations() throws ParserException {
        tokenizer.nextToken();
        TripleExpression currentResult = null;
        switch (tokenizer.getCurrentToken()) {
            case CONST:
                try {
                    currentResult = new Const(Integer.parseInt(tokenizer.getToken()));
                } catch (NumberFormatException e) {
                    throw new OverflowException(tokenizer.getToken());
                }
                tokenizer.nextToken();
                break;
            case VARIABLE:
                currentResult = new Variable(tokenizer.getToken());
                tokenizer.nextToken();
                break;
            case OPENING_BRACKET:
                currentResult = lowestPriorityOperations();
                tokenizer.nextToken();
                break;
            case NEGATE:
                currentResult = new CheckedNegate(highPriorityOperations());
                break;
            case MODULUS:
                currentResult = new CheckedModulus(highPriorityOperations());
                break;
            case SQUARE_ROOT:
                currentResult = new CheckedSquareRoot(highPriorityOperations());
                break;
            case HIGH:
                currentResult = new CheckedHigh(highPriorityOperations());
                break;
            case LOW:
                currentResult = new CheckedLow(highPriorityOperations());
                break;
            case CLOSING_BRACKET:
        }
        return currentResult;
    }

    private TripleExpression mediumPriorityOperations() throws ParserException {
        TripleExpression currentResult = highPriorityOperations();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case MULTIPLY:
                    currentResult = new CheckedMultiply(currentResult, highPriorityOperations());
                    break;
                case DIVIDE:
                    currentResult = new CheckedDivide(currentResult, highPriorityOperations());
                    break;
                default:
                    return currentResult;
            }
        }
    }

    private TripleExpression lowPriorityOperations() throws ParserException {
        TripleExpression currentResult = mediumPriorityOperations();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case ADD:
                    currentResult = new CheckedAdd(currentResult, mediumPriorityOperations());
                    break;
                case SUBTRACT:
                    currentResult = new CheckedSubtract(currentResult, mediumPriorityOperations());
                    break;
                default:
                    return currentResult;
            }
        }
    }

    private TripleExpression lowestPriorityOperations() throws ParserException {
        TripleExpression currentResult = lowPriorityOperations();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case MAXIMUM:
                    currentResult = new CheckedMaximum(currentResult, lowPriorityOperations());
                    break;
                case MINIMUM:
                    currentResult = new CheckedMinimum(currentResult, lowPriorityOperations());
                    break;
                default:
                    return currentResult;
            }
        }
    }

    public TripleExpression parse(String formula) throws ParserException, NullPointerException {
        if (formula == null) {
            throw new NullPointerException("Input string is null");
        }
        tokenizer = new Tokenizer(formula);
        TripleExpression result = lowestPriorityOperations();
        int positionOfOddBracket = tokenizer.checkForCorrectBracketsSequence();
        if (positionOfOddBracket >= 0) {
            throw new OddOpeningBracketException(formula, positionOfOddBracket);
        }
        return result;
    }
}