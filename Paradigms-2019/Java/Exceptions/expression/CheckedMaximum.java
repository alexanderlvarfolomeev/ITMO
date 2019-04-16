package expression;

public class CheckedMaximum extends AbstractBinaryExpression {
    public CheckedMaximum(TripleExpression firstArgument, TripleExpression secondArgument) {
        super(firstArgument, secondArgument);
    }

    public int calculate(int firstNumber, int secondNumber) {
        return firstNumber > secondNumber ? firstNumber : secondNumber;
    }
}
