package expression;

public class Variable implements TripleExpression{
    private final char name;

    public Variable(char name) {
        this.name = name;
    }

    public int evaluate(int valueOfX, int valueOfY, int valueOfZ) {
        switch (name) {
            case 'x':
                return valueOfX;
            case 'y':
                return valueOfY;
            case 'z':
                return valueOfZ;
            default:
                return 0;
        }
    }
}
