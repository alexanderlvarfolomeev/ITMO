package expression;

public enum BinaryOperation {
    ADD {
        int calculate(int firstArgument, int secondArgument) {
            return firstArgument + secondArgument;
        }
    },
    SUBTRACT {
        int calculate(int firstArgument, int secondArgument) {
            return firstArgument - secondArgument;
        }
    },
    MULTIPLY {
        int calculate(int firstArgument, int secondArgument) {
            return firstArgument * secondArgument;
        }
    },
    DIVIDE {
        int calculate(int firstArgument, int secondArgument) {
            return firstArgument / secondArgument;
        }
    },
    AND {
        int calculate(int firstArgument, int secondArgument) {
            return firstArgument & secondArgument;
        }
    },
    XOR {
        int calculate(int firstArgument, int secondArgument) {
            return firstArgument ^ secondArgument;
        }
    },
    OR {
        int calculate(int firstArgument, int secondArgument) {
            return firstArgument | secondArgument;
        }
    },
    LEFT_SHIFT {
        int calculate(int firstArgument, int secondArgument) {
            return firstArgument << secondArgument;
        }
    },
    RIGHT_SHIFT {
        int calculate(int firstArgument, int secondArgument) {
            return firstArgument >> secondArgument;
        }
    };

    abstract int calculate(int firstArgument, int secondArgument);
}
