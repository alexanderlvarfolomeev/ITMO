package expression;

public class BinaryExpression implements TripleExpression {
    private final TripleExpression firstArgument;
    private final TripleExpression secondArgument;
    private final BinaryOperation operation;

    public BinaryExpression(TripleExpression firstArgument, TripleExpression secondArgument, BinaryOperation operation) {
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
        this.operation = operation;
    }

    public int evaluate(int valueOfX, int valueOfY, int valueOfZ) {
        return operation.calculate(firstArgument.evaluate(valueOfX, valueOfY, valueOfZ),
                secondArgument.evaluate(valueOfX, valueOfY, valueOfZ));
    }
}
