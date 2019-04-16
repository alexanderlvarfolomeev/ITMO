package expression.operations;

import expression.exceptions.DivisionByZeroException;

public class LongOperations implements Operations<Long> {

    public Long toConst(String value) {
        return Long.parseLong(value);
    }

    public Long add(Long firstArgument, Long secondArgument) {
        return firstArgument + secondArgument;
    }

    public Long subtract(Long firstArgument, Long secondArgument) {
        return firstArgument - secondArgument;
    }

    public Long multiply(Long firstArgument, Long secondArgument) {
        return firstArgument * secondArgument;
    }

    public Long divide(Long firstArgument, Long secondArgument) throws DivisionByZeroException {
        if (secondArgument == 0) {
            throw new DivisionByZeroException(firstArgument, secondArgument);
        }
        return firstArgument / secondArgument;
    }

    public Long maximum(Long firstArgument, Long secondArgument) {
        return Long.max(firstArgument, secondArgument);
    }

    public Long minimum(Long firstArgument, Long secondArgument) {
        return Long.min(firstArgument, secondArgument);
    }

    public Long modulus(Long firstArgument, Long secondArgument) throws DivisionByZeroException {
        if (secondArgument == 0) {
            throw new DivisionByZeroException(firstArgument, secondArgument);
        }
        return firstArgument % secondArgument;
    }

    public Long negate(Long argument) {
        return -argument;
    }

    public Long absolute(Long argument) {
        return argument >= 0 ? argument : -argument;
    }

    public Long square(Long argument) {
        return argument * argument;
    }
}
