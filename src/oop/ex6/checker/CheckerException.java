package oop.ex6.checker;

public abstract class CheckerException extends Exception {

    public CheckerException(String message) {
        super(message);
    }

    public CheckerException(String message, Exception exception) {
        super(message, exception);
    }
}
