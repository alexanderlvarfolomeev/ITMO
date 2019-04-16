package expression.operations;

public class FloatOperations implements Operations<Float> {

    public Float toConst(String value) {
        return Float.parseFloat(value);
    }

    public Float add(Float firstArgument, Float secondArgument) {
        return firstArgument + secondArgument;
    }

    public Float subtract(Float firstArgument, Float secondArgument) {
        return firstArgument - secondArgument;
    }

    public Float multiply(Float firstArgument, Float secondArgument) {
        return firstArgument * secondArgument;
    }

    public Float divide(Float firstArgument, Float secondArgument) {
        return firstArgument / secondArgument;
    }

    public Float maximum(Float firstArgument, Float secondArgument) {
        return Float.max(firstArgument, secondArgument);
    }

    public Float minimum(Float firstArgument, Float secondArgument) {
        return Float.min(firstArgument, secondArgument);
    }

    public Float modulus(Float firstArgument, Float secondArgument) {
        return firstArgument % secondArgument;
    }

    public Float negate(Float argument) {
        return -argument;
    }

    public Float absolute(Float argument) {
        return argument >= 0 ? argument : -argument;
    }

    public Float square(Float argument) {
        return argument * argument;
    }
}
