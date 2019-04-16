package expression;

public enum UnaryOperation {
    NEGATE {
        int calculate(int argument) {
            return -argument;
        }
    },
    NOT {
        int calculate(int argument) {
            return ~argument;
        }
    },
    COUNT {
        int calculate(int argument) {
            return Integer.bitCount(argument);
        }
    };

    abstract int calculate(int argument);
}
