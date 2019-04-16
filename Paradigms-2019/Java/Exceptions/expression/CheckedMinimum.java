package expression;

public class CheckedMinimum extends AbstractBinaryExpression {
    public CheckedMinimum(TripleExpression firstArgument, TripleExpression secondArgument) {
        super(firstArgument, secondArgument);
    }

    public int calculate(int firstNumber, int secondNumber) {
        return firstNumber < secondNumber ? firstNumber : secondNumber;
    }
}