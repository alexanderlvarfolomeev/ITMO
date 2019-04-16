package expression;

public class UnaryExpression implements TripleExpression {
    private final TripleExpression argument;
    private final UnaryOperation operation;

    public UnaryExpression(TripleExpression argument, UnaryOperation operation) {
        this.argument = argument;
        this.operation = operation;
    }

    public int evaluate(int valueOfX, int valueOfY, int valueOfZ) {
        return operation.calculate(argument.evaluate(valueOfX, valueOfY, valueOfZ));
    }
}