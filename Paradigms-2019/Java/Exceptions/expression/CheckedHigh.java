package expression;

public class CheckedHigh extends AbstractUnaryExpression{
    public CheckedHigh(TripleExpression argument) {
        super(argument);
    }

    public int calculate(int number) {
        return Integer.highestOneBit(number);
    }
}
