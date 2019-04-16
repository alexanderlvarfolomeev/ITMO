package expression.operations;

import expression.exceptions.DivisionByZeroException;

import java.math.BigInteger;

public class BigIntegerOperations implements Operations<BigInteger> {

    public BigInteger toConst(String value) {
        return BigInteger.valueOf(Long.parseLong(value));
    }

    public BigInteger add(BigInteger firstArgument, BigInteger secondArgument) {
        return firstArgument.add(secondArgument);
    }

    public BigInteger subtract(BigInteger firstArgument, BigInteger secondArgument) {
        return firstArgument.subtract(secondArgument);
    }

    public BigInteger multiply(BigInteger firstArgument, BigInteger secondArgument) {
        return firstArgument.multiply(secondArgument);
    }

    public BigInteger divide(BigInteger firstArgument, BigInteger secondArgument) throws DivisionByZeroException {
        if (secondArgument.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException(firstArgument, secondArgument);
        }
        return firstArgument.divide(secondArgument);
    }

    public BigInteger maximum(BigInteger firstArgument, BigInteger secondArgument) {
        return firstArgument.max(secondArgument);
    }

    public BigInteger minimum(BigInteger firstArgument, BigInteger secondArgument) {
        return firstArgument.min(secondArgument);
    }

    public BigInteger modulus(BigInteger firstArgument, BigInteger secondArgument) throws DivisionByZeroException {
        if (secondArgument.compareTo(BigInteger.ZERO) <= 0) {
            throw new DivisionByZeroException(firstArgument, secondArgument);
        }
        return firstArgument.mod(secondArgument);
    }

    public BigInteger negate(BigInteger argument) {
        return argument.negate();
    }

    public BigInteger absolute(BigInteger argument) {
        return argument.abs();
    }

    public BigInteger square(BigInteger argument) {
        return argument.multiply(argument);
    }
}
