package expression.operations;

import expression.exceptions.DivisionByZeroException;

public class ByteOperations implements Operations<Byte> {

    public Byte toConst(String value) {
        return (byte) Integer.parseInt(value);
    }

    public Byte add(Byte firstArgument, Byte secondArgument) {
        return (byte) (firstArgument + secondArgument);
    }

    public Byte subtract(Byte firstArgument, Byte secondArgument) {
        return (byte) (firstArgument - secondArgument);
    }

    public Byte multiply(Byte firstArgument, Byte secondArgument) {
        return (byte) (firstArgument * secondArgument);
    }

    public Byte divide(Byte firstArgument, Byte secondArgument) throws DivisionByZeroException {
        if (secondArgument == 0) {
            throw new DivisionByZeroException(firstArgument, secondArgument);
        }
        return (byte) (firstArgument / secondArgument);
    }

    public Byte maximum(Byte firstArgument, Byte secondArgument) {
        return firstArgument > secondArgument ? firstArgument : secondArgument;
    }

    public Byte minimum(Byte firstArgument, Byte secondArgument) {
        return firstArgument < secondArgument ? firstArgument : secondArgument;
    }

    public Byte modulus(Byte firstArgument, Byte secondArgument) throws DivisionByZeroException {
        if (secondArgument == 0) {
            throw new DivisionByZeroException(firstArgument, secondArgument);
        }
        return (byte) (firstArgument % secondArgument);
    }

    public Byte negate(Byte argument) {
        return (byte) (-argument);
    }

    public Byte absolute(Byte argument) {
        return argument >= 0 ? argument : negate(argument);
    }

    public Byte square(Byte argument) {
        return (byte) (argument * argument);
    }
}
