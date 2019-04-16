package expression.operations;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class IntegerOperations implements Operations<Integer> {
    private final boolean checked;

    public IntegerOperations(boolean checked) {
        this.checked = checked;
    }

    public Integer toConst(String value) {
        return Integer.parseInt(value);
    }

    public Integer add(Integer firstArgument, Integer secondArgument) throws OverflowException {
        if (checked && (secondArgument > 0 && Integer.MAX_VALUE - secondArgument < firstArgument
                || secondArgument < 0 && Integer.MIN_VALUE - secondArgument > firstArgument)) {
            throw new OverflowException(firstArgument + " + " + secondArgument);
        }
        return firstArgument + secondArgument;
    }

    public Integer subtract(Integer firstArgument, Integer secondArgument) throws OverflowException {

        if (checked && (secondArgument < 0 && Integer.MAX_VALUE + secondArgument < firstArgument
                || secondArgument > 0 && Integer.MIN_VALUE + secondArgument > firstArgument)) {
            throw new OverflowException(firstArgument + " - " + secondArgument);
        }
        return firstArgument - secondArgument;
    }

    public Integer multiply(Integer firstArgument, Integer secondArgument) throws OverflowException {
        if (checked && (firstArgument > 0 && secondArgument > 0 && firstArgument > Integer.MAX_VALUE / secondArgument
                || firstArgument < 0 && secondArgument < 0 && firstArgument < Integer.MAX_VALUE / secondArgument
                || firstArgument < 0 && secondArgument > 0 && firstArgument < Integer.MIN_VALUE / secondArgument
                || firstArgument > 0 && secondArgument < 0 && secondArgument < Integer.MIN_VALUE / firstArgument)) {
            throw new OverflowException(firstArgument + " * " + secondArgument);
        }
        return firstArgument * secondArgument;
    }

    public Integer divide(Integer firstArgument, Integer secondArgument)
            throws DivisionByZeroException, OverflowException {

        if (secondArgument == 0) {
            throw new DivisionByZeroException(firstArgument, secondArgument);
        }

        if (checked && firstArgument == Integer.MIN_VALUE && secondArgument == -1) {
            throw new OverflowException(firstArgument + " / " + secondArgument);
        }
        return firstArgument / secondArgument;
    }

    public Integer maximum(Integer firstArgument, Integer secondArgument) {
        return Integer.max(firstArgument, secondArgument);
    }

    public Integer minimum(Integer firstArgument, Integer secondArgument) {
        return Integer.min(firstArgument, secondArgument);
    }

    public Integer modulus(Integer firstArgument, Integer secondArgument) throws DivisionByZeroException {
        if (secondArgument == 0) {
            throw new DivisionByZeroException(firstArgument, secondArgument);
        }
        return firstArgument % secondArgument;
    }

    public Integer negate(Integer argument) throws OverflowException {
        if (checked && argument == Integer.MIN_VALUE) {
            throw new OverflowException("|" + argument + "|");
        }
        return -argument;
    }

    public Integer absolute(Integer argument) throws OverflowException {
        if (checked && argument == Integer.MIN_VALUE) {
            throw new OverflowException("|" + argument + "|");
        }
        return argument >= 0 ? argument : -argument;
    }

    public Integer square(Integer argument) throws OverflowException {
        if (checked && (argument > 46340 || argument < -46340)) {
            throw new OverflowException(argument + " ^ 2");
        }
        return argument * argument;
    }
}
