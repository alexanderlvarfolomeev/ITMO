package expression;

public class CheckedLow extends AbstractUnaryExpression {
    public CheckedLow(TripleExpression argument) {
        super(argument);
    }

    public int calculate(int number) {
        return Integer.lowestOneBit(number);
    }
}
