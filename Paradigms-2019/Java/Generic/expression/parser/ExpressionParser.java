package expression.parser;

import expression.*;
import expression.exceptions.*;
import expression.operations.Operations;

public class ExpressionParser<T extends Number & Comparable<T>> implements Parser<T> {

    private Tokenizer tokenizer;
    private final Operations<T> operations;

    private TripleExpression<T> highPriorityOperations() throws ParserException {
        tokenizer.nextToken();
        TripleExpression<T> currentResult = null;
        switch (tokenizer.getCurrentToken()) {
            case CONST:
                currentResult = new Const<>(operations.toConst(tokenizer.getToken()));
                tokenizer.nextToken();
                break;
            case VARIABLE:
                currentResult = new Variable<>(tokenizer.getToken());
                tokenizer.nextToken();
                break;
            case OPENING_BRACKET:
                currentResult = lowestPriorityOperations();
                tokenizer.nextToken();
                break;
            case NEGATE:
                currentResult = new CheckedNegate<>(highPriorityOperations(), operations);
                break;
            case ABSOLUTE:
                currentResult = new CheckedAbsolute<>(highPriorityOperations(), operations);
                break;
            case SQUARE:
                currentResult = new CheckedSquare<>(highPriorityOperations(), operations);
                break;
            case CLOSING_BRACKET:
        }
        return currentResult;
    }

    private TripleExpression<T> mediumPriorityOperations() throws ParserException {
        TripleExpression<T> currentResult = highPriorityOperations();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case MULTIPLY:
                    currentResult = new CheckedMultiply<>(currentResult, highPriorityOperations(), operations);
                    break;
                case DIVIDE:
                    currentResult = new CheckedDivide<>(currentResult, highPriorityOperations(), operations);
                    break;
                case MODULUS:
                    currentResult = new CheckedModulus<>(currentResult, highPriorityOperations(), operations);
                    break;
                default:
                    return currentResult;
            }
        }
    }

    private TripleExpression<T> lowPriorityOperations() throws ParserException {
        TripleExpression<T> currentResult = mediumPriorityOperations();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case ADD:
                    currentResult = new CheckedAdd<>(currentResult, mediumPriorityOperations(), operations);
                    break;
                case SUBTRACT:
                    currentResult = new CheckedSubtract<>(currentResult, mediumPriorityOperations(), operations);
                    break;
                default:
                    return currentResult;
            }
        }
    }

    private TripleExpression<T> lowestPriorityOperations() throws ParserException {
        TripleExpression<T> currentResult = lowPriorityOperations();
        while (true) {
            switch (tokenizer.getCurrentToken()) {
                case MAXIMUM:
                    currentResult = new CheckedMaximum<>(currentResult, lowPriorityOperations(), operations);
                    break;
                case MINIMUM:
                    currentResult = new CheckedMinimum<>(currentResult, lowPriorityOperations(), operations);
                    break;
                default:
                    return currentResult;
            }
        }
    }
    public ExpressionParser(Operations<T> operations) {
        this.operations = operations;
    }

    public TripleExpression<T> parse(String formula) throws ParserException, NullPointerException {
        if (formula == null) {
            throw new NullPointerException("Input string is null");
        }
        tokenizer = new Tokenizer(formula);
        TripleExpression<T> result = lowestPriorityOperations();
        int positionOfOddBracket = tokenizer.checkForCorrectBracketsSequence();
        if (positionOfOddBracket >= 0) {
            throw new OddOpeningBracketException(formula, positionOfOddBracket);
        }
        return result;
    }
}