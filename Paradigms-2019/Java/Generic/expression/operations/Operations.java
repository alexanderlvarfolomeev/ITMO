package expression.operations;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public interface Operations<T extends Number & Comparable<T>> {
    T toConst(String x) throws OverflowException;

    T add(T x, T y) throws OverflowException;

    T subtract(T x, T y) throws OverflowException;

    T multiply(T x, T y) throws OverflowException;

    T divide(T x, T y) throws DivisionByZeroException, OverflowException;

    T maximum(T x, T y);

    T minimum(T x, T y);

    T negate(T x) throws OverflowException;

    T modulus(T x, T y) throws DivisionByZeroException;

    T absolute(T x) throws OverflowException;

    T square(T x) throws OverflowException;
}
