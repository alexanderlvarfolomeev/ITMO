package expression;

abstract class AbstractBinaryExpression extends AbstractExpression {
    AbstractExpression firstArgument;
    AbstractExpression secondArgument;

    public AbstractBinaryExpression(AbstractExpression firstArgument, AbstractExpression secondArgument) {
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
    }
}
