package expression;

import expression.exceptions.ExpressionException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface TripleExpression<T extends Number & Comparable<T>> {
    T evaluate(T x, T y, T z) throws ExpressionException;
}