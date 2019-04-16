"use strict";
const cnst = a => {
    return () => a;
};

const one = cnst(1);
const two = cnst(2);
const variable = a => {
    switch (a) {
        case "x":
            return (x, y, z) => x;
        case "y":
            return (x, y, z) => y;
        case "z":
            return (x, y, z) => z;
    }
};

const unaryOperation = operation => a => {
    return (x, y, z) => operation(a(x, y, z));
};

const binaryOperation = operation => (a, b) => {
    return (x, y, z) => operation(a(x, y, z), b(x, y, z));
};

const negate = unaryOperation(a => (-a));
const abs = unaryOperation(a => Math.abs(a));

const add = binaryOperation((a, b) => a + b);
const subtract = binaryOperation((a, b) => a - b);
const multiply = binaryOperation((a, b) => a * b);
const divide = binaryOperation((a, b) => a / b);

const iff = (a, b, c) => (x, y, z) => {
    if (a(x, y, z) >= 0) {
        return b(x, y, z);
    } else {
        return c(x, y, z);
    }
};

const argumentCount = new Map([
    ["negate", 1],
    ["abs", 1],
    ["+", 2],
    ["-", 2],
    ["*", 2],
    ["/", 2],
    ["iff", 3]
]);

const getOperation = (accumulator, currentValue) => {
    switch (currentValue) {
        case "x":
        case "y":
        case "z":
            accumulator.push(variable(currentValue));
            break;
        case "one":
            accumulator.push(one);
            break;
        case "two":
            accumulator.push(two);
            break;
        case "+":
            accumulator.push(add(...accumulator.slice(-argumentCount.get(currentValue))));
            break;
        case "-":
            accumulator.push(subtract(...accumulator.slice(-argumentCount.get(currentValue))));
            break;
        case "*":
            accumulator.push(multiply(...accumulator.slice(-argumentCount.get(currentValue))));
            break;
        case "/":
            accumulator.push(divide(...accumulator.slice(-argumentCount.get(currentValue))));
            break;
        case "negate":
            accumulator.push(negate(...accumulator.slice(-argumentCount.get(currentValue))));
            break;
        case "abs":
            accumulator.push(abs(...accumulator.slice(-argumentCount.get(currentValue))));
            break;
        case "iff":
            accumulator.push(iff(...accumulator.slice(-argumentCount.get(currentValue))));
            break;
        case "":
            break;
        default:
            accumulator.push(cnst(Number(currentValue)));
    }
    if (argumentCount.has(currentValue)) {
        accumulator.splice(-argumentCount.get(currentValue) - 1, argumentCount.get(currentValue));
    }
    return accumulator;
};

const parse = string => {
    let tokens = string.split(" ");
    let k = tokens.reduce(getOperation, []);
    return k.pop();
};
