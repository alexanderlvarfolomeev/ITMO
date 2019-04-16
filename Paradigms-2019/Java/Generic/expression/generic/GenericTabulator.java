package expression.generic;

import expression.*;
import expression.exceptions.ExpressionException;
import expression.exceptions.ParserException;
import expression.operations.*;
import expression.parser.ExpressionParser;

import java.util.Map;


public class GenericTabulator implements Tabulator {

    private static final Map<String, ? extends Operations<? extends Number>> type = Map.of(
            "i", new IntegerOperations(true),
            "u", new IntegerOperations(false),
            "d", new DoubleOperations(),
            "bi", new BigIntegerOperations(),
            "f", new FloatOperations(),
            "b", new ByteOperations(),
            "l", new LongOperations(),
            "s", new ShortOperations()
    );

    public Object[][][] tabulate(String mode, String formula, int x1, int x2, int y1, int y2, int z1, int z2)
            throws ParserException {
        return calculate(type.get(mode), formula, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number & Comparable<T>> Object[][][] calculate(Operations<T> operations, String formula,
                                                                      int x1, int x2, int y1, int y2, int z1, int z2)
            throws ParserException {
        TripleExpression<T> expression = new ExpressionParser<>(operations).parse(formula);
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    try {
                        result[x - x1][y - y1][z - z1] = expression.evaluate
                                (
                                        operations.toConst(Integer.toString(x)),
                                        operations.toConst(Integer.toString(y)),
                                        operations.toConst(Integer.toString(z))
                                );
                    } catch (ExpressionException e) {
                        result[x - x1][y - y1][z - z1] = null;
                    }
                }
            }
        }
        return result;
    }
}
