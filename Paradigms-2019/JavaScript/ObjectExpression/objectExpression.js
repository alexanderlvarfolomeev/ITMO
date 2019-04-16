"use strict";

function ParsingException(message) {
    this.message = message;
}

ParsingException.prototype = Error.prototype;

function EmptyInputException() {
    ParsingException.call(this, "The input string is empty.");
}

function OddOperandException(expression) {
    ParsingException.call(this, "There are too many operands in the expression: \n" + expression);
}

function NoOperandException(expression) {
    ParsingException.call(this, "There is no operands in the expression: \n" + expression);
}

function OddOperationException(expression) {
    ParsingException.call(this, "There is too many operations in the expression: \n" + expression);
}

function OddOpeningBracketException(expression) {
    ParsingException.call(this, "There are odd opening brackets in the expression: \n" + expression);
}

function createPointer(index, count) {
    let result = "";
    for (let i = 0; i < index; i++) {
        result += " ";
    }
    for (let i = 0; i < count; i++) {
        result += "^";
    }
    return result;
}

function OddClosingBracketException(expression, index) {
    ParsingException.call(this, "There is odd closing bracket in the expression: \n" +
        expression + "\n" + createPointer(index, 1));
}

function UnexpectedElementException(expression, index, count) {
    ParsingException.call(this, "There is unexpected element in the expression: \n" +
        expression + "\n" + createPointer(index, count));
}

function ArgumentsCountException(operation, args, type) {
    let result = "";
    for (const arg of args) {
        result += type ? " " + arg.prefix() : arg.postfix() + " ";
    }
    type ? result = "(" + operation + result + ")" : "(" + result + operation + ")";
    ParsingException.call(this, "There is invalid count of arguments in the expression: \n" + result);
}

function Const(value) {
    this.value = value;
    this.getValue = function () {
        return this.evaluate();
    };
    this.toString = function () {
        return value.toString();
    };
    this.prefix = function () {
        return this.toString();
    };
    this.postfix = function () {
        return this.toString();
    };
    this.evaluate = function () {
        return value;
    };
    this.diff = function () {
        return new Const(0);
    };
    this.simplify = function () {
        return this;
    }
}

const number = new Map([
    ["x", 0],
    ["y", 1],
    ["z", 2]
]);

function Variable(value) {
    this.value = value;
    this.toString = function () {
        return value;
    };
    this.prefix = function () {
        return this.toString();
    };
    this.postfix = function () {
        return this.toString();
    };
    this.evaluate = function (...args) {
        return args[number.get(value)];
    };
    this.diff = function (variable) {
        return value === variable ? new Const(1) : new Const(0);
    };
    this.simplify = function () {
        return this;
    }
}

function UnaryOperation(argument, operation, op) {
    this.argument = argument;
    this.operation = operation;
    this.op = op;
}

UnaryOperation.prototype.toString = function () {
    return this.argument.toString() + " " + this.op;
};
UnaryOperation.prototype.prefix = function () {
    return "(" + this.op + " " + this.argument.prefix() + ")";
};
UnaryOperation.prototype.postfix = function () {
    return "(" + this.argument.postfix() + " " + this.op + ")";
};
UnaryOperation.prototype.evaluate = function (x, y, z) {
    return this.operation(this.argument.evaluate(x, y, z));
};

function Negate(argument) {
    UnaryOperation.call(this, argument, (a) => -a, "negate");
    this.getArgument = function () {
        return argument;
    };
    this.diff = function (variable) {
        return new Negate(argument.diff(variable));
    };
    this.simplify = function () {

        const argument = this.argument.simplify();
        if (argument instanceof Const) {
            return new Const(-argument.getValue());
        }
        if (argument instanceof Negate) {
            return argument.getArgument();
        }
        return this;
    }
}

Negate.prototype = Object.create(UnaryOperation.prototype);

function ArcTan(argument) {

    UnaryOperation.call(this, argument, Math.atan, "atan");
    this.diff = function (variable) {
        return new Divide(argument.diff(variable), new Add(new Multiply(argument, argument), new Const(1)));
    };
    this.simplify = function () {
        const argument = this.argument.simplify();
        if (argument instanceof Const) {
            return new Const(Math.atan(argument.getValue()));
        }
        return this;
    }
}

ArcTan.prototype = Object.create(UnaryOperation.prototype);

function Sinh(argument) {

    UnaryOperation.call(this, argument, Math.sinh, "sinh");
    this.diff = function (variable) {
        return new Multiply(new Cosh(argument), argument.diff(variable));
    };
    this.simplify = function () {
        const argument = this.argument.simplify();
        if (argument instanceof Const) {
            return new Const(Math.sinh(argument.getValue()));
        }
        return this;
    }
}

Sinh.prototype = Object.create(UnaryOperation.prototype);

function Cosh(argument) {
    UnaryOperation.call(this, argument, Math.cosh, "cosh");
    this.diff = function (variable) {
        return new Multiply(new Sinh(argument), argument.diff(variable));
    };
    this.simplify = function () {
        const argument = this.argument.simplify();
        if (argument instanceof Const) {
            return new Const(Math.cosh(argument.getValue()));
        }
        return this;
    };
}

Cosh.prototype = Object.create(UnaryOperation.prototype);

function BinaryOperation(first, second, operation, op) {
    this.first = first;
    this.second = second;
    this.operation = operation;
    this.op = op;
}

BinaryOperation.prototype.toString = function () {
    return this.first.toString() + " " + this.second.toString() + " " + this.op;
};
BinaryOperation.prototype.prefix = function () {
    return "(" + this.op + " " + this.first.prefix() + " " + this.second.prefix() + ")";
};
BinaryOperation.prototype.postfix = function () {
    return "(" + this.first.postfix() + " " + this.second.postfix() + " " + this.op + ")";
};
BinaryOperation.prototype.evaluate = function (x, y, z) {
    return this.operation(this.first.evaluate(x, y, z), this.second.evaluate(x, y, z));
};

function Add(first, second) {
    BinaryOperation.call(this, first, second, (a, b) => a + b, "+");
    this.diff = function (variable) {
        return new Add(first.diff(variable), second.diff(variable));
    };
    this.simplify = function () {
        const first = this.first.simplify();
        const second = this.second.simplify();
        if (first instanceof Const && second instanceof Const) {
            return new Const(first.getValue() + second.getValue());
        }
        if (first.toString() === "0") {
            return second;
        }
        if (second.toString() === "0") {
            return first;
        }
        if (first.toString() + " negate" === second.toString() ||
            first.toString() === second.toString() + " negate") {
            return new Const(0);
        }
        return new Add(first, second);
    }
}

Add.prototype = Object.create(BinaryOperation.prototype);

function Subtract(first, second) {
    BinaryOperation.call(this, first, second, (a, b) => a - b, "-");
    this.diff = function (variable) {
        return new Subtract(first.diff(variable), second.diff(variable));
    };
    this.simplify = function () {
        const first = this.first.simplify();
        const second = this.second.simplify();
        if (first instanceof Const && second instanceof Const) {
            return new Const(first.getValue() - second.getValue());
        }
        if (first.toString() === "0") {
            return (new Negate(second)).simplify();
        }
        if (second.toString() === "0") {
            return first;
        }
        if (first.toString() === second.toString()) {
            return new Const(0);
        }
        return new Subtract(first, second);
    }
}

Subtract.prototype = Object.create(BinaryOperation.prototype);

function Multiply(first, second) {
    BinaryOperation.call(this, first, second, (a, b) => a * b, "*");
    this.diff = function (variable) {
        return new Add(
            new Multiply(first.diff(variable), second),
            new Multiply(first, second.diff(variable)));
    };
    this.simplify = function () {
        const first = this.first.simplify();
        const second = this.second.simplify();
        if (first instanceof Const && second instanceof Const) {
            return new Const(first.getValue() * second.getValue());
        }
        if (first.toString() === "0" || second.toString() === "0") {
            return new Const(0);
        }
        if (first.toString() === "1") {
            return second;
        }
        if (second.toString() === "1") {
            return first;
        }
        return new Multiply(first, second);
    }
}

Multiply.prototype = Object.create(BinaryOperation.prototype);

function Divide(first, second) {
    BinaryOperation.call(this, first, second, (a, b) => a / b, "/");
    this.diff = function (variable) {
        return new Divide(
            new Subtract(
                new Multiply(first.diff(variable), second),
                new Multiply(first, second.diff(variable))),
            new Multiply(second, second));
    };
    this.simplify = function () {
        const first = this.first.simplify();
        const second = this.second.simplify();
        if (first instanceof Const && second instanceof Const) {
            return new Const(first.getValue() / second.getValue());
        }
        if (first.toString() === "0" || second.toString() === "1") {
            return first;
        }
        return new Divide(first, second);
    }
}

Divide.prototype = Object.create(BinaryOperation.prototype);

function ArcTan2(first, second) {
    BinaryOperation.call(this, first, second, (a, b) => Math.atan2(a, b), "atan2");
    this.diff = function (variable) {
        return new Divide(
            new Subtract(
                new Multiply(first.diff(variable), second),
                new Multiply(first, second.diff(variable))),
            new Add(
                new Multiply(first, first),
                new Multiply(second, second))
        );
    };
    this.simplify = function () {
        const first = this.first.simplify();
        const second = this.second.simplify();
        if (first instanceof Const && second instanceof Const) {
            return new Const(Math.atan2(first.getValue(), second.getValue()));
        }
        if (first.toString() === "0") {
            return first;
        }
        if (second.toString() === "1") {
            return new ArcTan(first);
        }
        return new ArcTan2(first, second);
    }
}

ArcTan2.prototype = Object.create(BinaryOperation.prototype);

function MultipleArgumentsOperation(operation, op, args) {
    this.args = args;
    this.operation = operation;
    this.op = op;
}

MultipleArgumentsOperation.prototype.toString = function () {
    return this.args.reduce(function (string, element) {
        return string + element.toString() + " ";
    }, "") + this.op;
};
MultipleArgumentsOperation.prototype.prefix = function () {
    if (this.args.length === 0) {
        return "(" + this.op + " )";
    }
    return "(" + this.op + this.args.reduce(function (string, element) {
        return string + " " + element.prefix();
    }, "") + ")";
};
MultipleArgumentsOperation.prototype.postfix = function () {
    if (this.args.length === 0) {
        return "( " + this.op + ")";
    }
    return "(" + this.args.reduce(function (string, element) {
        return string + element.postfix() + " ";
    }, "") + this.op + ")";
};
MultipleArgumentsOperation.prototype.evaluate = function (x, y, z) {
    return this.operation(x, y, z);
};

function Sum(...args) {
    MultipleArgumentsOperation.call(this, (x, y, z) => {
        return this.args.reduce(function (result, argument) {
            return result + argument.evaluate(x, y, z);
        }, 0)
    }, "sum", args);
    this.diff = function (variable) {
        return new Sum(...(args.reduce(function (elements, element) {
            elements.push(element.diff(variable));
            return elements;
        }, [])));
    };
}

Sum.prototype = Object.create(MultipleArgumentsOperation.prototype);

function Avg(...args) {
    MultipleArgumentsOperation.call(this, (x, y, z) => {
        return this.args.reduce(function (result, argument) {
            return result + argument.evaluate(x, y, z);
        }, 0) / this.args.length
    }, "avg", args);
    this.diff = function (variable) {
        return new Avg(...(args.reduce(function (elements, element) {
            elements.push(element.diff(variable));
            return elements;
        }, [])));
    };
}

Avg.prototype = Object.create(MultipleArgumentsOperation.prototype);

function Sumexp(...args) {
    MultipleArgumentsOperation.call(this, (x, y, z) => {
        return this.args.reduce(function (result, argument) {
            return result + Math.exp(argument.evaluate(x, y, z));
        }, 0)
    }, "sumexp", args);
    this.diff = function (variable) {
        return new Sum(...args.reduce(function (elements, element) {
            elements.push(new Multiply(element.diff(variable), new Sumexp(element)));
            return elements;
        }, []));
    };
}

Sumexp.prototype = Object.create(MultipleArgumentsOperation.prototype);

function Softmax(...args) {
    MultipleArgumentsOperation.call(this, (x, y, z) => {
        return Math.exp(this.args[0].evaluate(x, y, z)) / (this.args.reduce(function (result, argument) {
            return result + Math.exp(argument.evaluate(x, y, z));
        }, 0))
    }, "softmax", args);
    this.diff = function (variable) {
        return new Divide(new Sumexp(this.args[0]), new Sumexp(...args)).diff(variable);
    };
}

Softmax.prototype = Object.create(MultipleArgumentsOperation.prototype);

const argumentCount = new Map([
    ["negate", 1],
    ["atan", 1],
    ["sinh", 1],
    ["cosh", 1],
    ["atan2", 2],
    ["+", 2],
    ["-", 2],
    ["*", 2],
    ["/", 2],
    ["sum", -1],
    ["avg", -1],
    ["sumexp", -1],
    ["softmax", -1]
]);

const constructors = new Map([
    ["negate", Negate],
    ["atan", ArcTan],
    ["sinh", Sinh],
    ["cosh", Cosh],
    ["atan2", ArcTan2],
    ["+", Add],
    ["-", Subtract],
    ["*", Multiply],
    ["/", Divide],
    ["sum", Sum],
    ["avg", Avg],
    ["sumexp", Sumexp],
    ["softmax", Softmax]
]);

const getOperation = (accumulator, currentValue) => {
    switch (currentValue) {
        case "x":
        case "y":
        case "z":
            accumulator.push(new Variable(currentValue));
            break;
        case "+":
        case "-":
        case "*":
        case "/":
        case "negate":
        case "atan":
        case "sinh":
        case "cosh":
        case "atan2":
            accumulator.push(new (constructors.get(currentValue))(...accumulator.slice(-argumentCount.get(currentValue))));
            break;
        case "":
            break;
        default:
            accumulator.push(new Const(Number(currentValue)));
    }
    if (argumentCount.has(currentValue)) {
        accumulator.splice(-argumentCount.get(currentValue) - 1, argumentCount.get(currentValue));
    }
    return accumulator;
};

const parse = string => {
    let tokens = string.split(" ");
    return tokens.reduce(getOperation, []).pop();
};

const parsePF = (string, type) => {

    if (string.length === 0) {
        throw new EmptyInputException();
    }

    let i = 0;
    let operationStack = [];
    let operandStack = [];

    const skipWhitespaces = () => {
        while (i < string.length && string[i] === " ") {
            i++;
        }
    };

    const getToken = () => {
        if (string[i] === "(" || string[i] === ")") {
            return string[i++];
        }
        let begin = i;
        while (i < string.length && /[^ ()]/.test(string[i])) {
            i++;
        }
        return string.substring(begin, i);
    };

    const trimExpression = () => {
        let first = operandStack.length - 1;
        while (operandStack[first] !== "(") {
            if (first < 0) {
                throw new OddClosingBracketException(string, i - 1);
            }
            first--;
        }
        let args = operandStack.slice(first + 1);
        operandStack.splice(first, operandStack.length);
        if (operationStack.length === 0) {
            if (operandStack.length === 0) {
                throw new NoOperandException(string);
            }
            throw new OddOperandException(string);
        }
        let operation = operationStack.pop();
        if (argumentCount.get(operation) > 0 && argumentCount.get(operation) !== args.length) {
            throw new ArgumentsCountException(operation, args, type);
        }
        operandStack.push(new (constructors.get(operation))(...args));
    };

    while (true) {
        skipWhitespaces();
        if (i >= string.length) {
            break;
        }
        let token = getToken();
        switch (token) {
            case ")":
                trimExpression();
                break;
            case "(":
                operandStack.push(token);
                break;
            case "+":
            case "-":
            case "*":
            case "/":
            case "negate":
            case "sum":
            case "avg":
            case "sumexp":
            case "softmax":
                operationStack.push(token);
                break;
            case "x":
            case "y":
            case "z":
                operandStack.push(new Variable(token));
                break;
            default:
                if (!isNaN(token)) {
                    operandStack.push(new Const(Number(token)));
                } else {
                    throw new UnexpectedElementException(string, i - token.length, token.length);
                }
        }
    }
    if (operandStack[0] === "(") {
        throw new OddOpeningBracketException(string, i - 1);
    }
    if (operandStack.length !== 1) {
        throw operandStack.length > 1 ? new OddOperandException(string) : new NoOperandException(string);
    }
    if (operationStack.length !== 0) {
        throw new OddOperationException(string);
    }
    return operandStack.pop();
};

const parsePrefix = (string) => parsePF(string, true);
const parsePostfix = (string) => parsePF(string, false);
