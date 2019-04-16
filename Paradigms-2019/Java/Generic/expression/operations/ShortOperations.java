package expression.operations;

import expression.exceptions.DivisionByZeroException;

public class ShortOperations implements Operations<Short> {

    public Short toConst(String value) {
        return (short) Integer.parseInt(value);
    }

    public Short add(Short firstArgument, Short secondArgument) {
        return (short) (firstArgument + secondArgument);
    }

    public Short subtract(Short firstArgument, Short secondArgument) {
        return (short) (firstArgument - secondArgument);
    }

    public Short multiply(Short firstArgument, Short secondArgument) {
        return (short) (firstArgument * secondArgument);
    }

    public Short divide(Short firstArgument, Short secondArgument) throws DivisionByZeroException {
        if (secondArgument == 0) {
            throw new DivisionByZeroException(firstArgument, secondArgument);
        }
        return (short) (firstArgument / secondArgument);
    }

    public Short maximum(Short firstArgument, Short secondArgument) {
        return firstArgument > secondArgument ? firstArgument : secondArgument;
    }

    public Short minimum(Short firstArgument, Short secondArgument) {
        return firstArgument < secondArgument ? firstArgument : secondArgument;
    }

    public Short modulus(Short firstArgument, Short secondArgument) throws DivisionByZeroException {
        if (secondArgument == 0) {
            throw new DivisionByZeroException(firstArgument, secondArgument);
        }
        return (short) (firstArgument % secondArgument);
    }

    public Short negate(Short argument) {
        return (short) (-argument);
    }

    public Short absolute(Short argument) {
        return argument >= 0 ? argument : negate(argument);
    }

    public Short square(Short argument) {
        return (short) (argument * argument);
    }
}
