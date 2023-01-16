package oop.ex6.checker;

public class IllegalLineException extends CheckerException {

    public IllegalLineException(String line) {
        super(line);
    }

    public IllegalLineException(String line, Exception exception) {
        super(line, exception);
    }
}
