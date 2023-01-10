package oop.ex6.checker;

public class IllegalLineTypeException extends CheckerException {

    private static final String ILLEGAL_LINE_MESSAGE = "%s is an illegal sjava line!";

    public IllegalLineTypeException(String line) {
        super(String.format(ILLEGAL_LINE_MESSAGE, line));
    }
}
