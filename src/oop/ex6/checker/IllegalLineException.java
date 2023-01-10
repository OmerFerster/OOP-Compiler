package oop.ex6.checker;

public class IllegalLineException extends CheckerException {

    private static final String ILLEGAL_LINE_MESSAGE = "%s is an illegal sjava line!";

    public IllegalLineException(String line) {
        super(String.format(ILLEGAL_LINE_MESSAGE, line));
    }
}
