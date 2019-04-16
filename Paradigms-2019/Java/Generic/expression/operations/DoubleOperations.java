package expression.operations;

public class DoubleOperations implements Operations<Double> {

    public Double toConst(String value) {
        return Double.parseDouble(value);
    }

    public Double add(Double firstArgument, Double secondArgument) {
        return firstArgument + secondArgument;
    }

    public Double subtract(Double firstArgument, Double secondArgument) {
        return firstArgument - secondArgument;
    }

    public Double multiply(Double firstArgument, Double secondArgument) {
        return firstArgument * secondArgument;
    }

    public Double divide(Double firstArgument, Double secondArgument) {
        return firstArgument / secondArgument;
    }

    public Double maximum(Double firstArgument, Double secondArgument) {
        return Double.max(firstArgument, secondArgument);
    }

    public Double minimum(Double firstArgument, Double secondArgument) {
        return Double.min(firstArgument, secondArgument);
    }

    public Double modulus(Double firstArgument, Double secondArgument) {
        return firstArgument % secondArgument;
    }

    public Double negate(Double argument) {
        return -argument;
    }

    public Double absolute(Double argument) {
        return argument >= 0 ? argument : -argument;
    }

    public Double square(Double argument) {
        return argument * argument;
    }
}
