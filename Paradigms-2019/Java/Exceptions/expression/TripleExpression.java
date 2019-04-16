package expression;

import expression.exceptions.ExpressionException;

public interface TripleExpression {
    int evaluate(int x, int y, int z) throws ExpressionException;
}