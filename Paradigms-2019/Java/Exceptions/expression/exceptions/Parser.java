package expression.exceptions;

import expression.TripleExpression;

public interface Parser{
    TripleExpression parse(String expression) throws Exception;
}
