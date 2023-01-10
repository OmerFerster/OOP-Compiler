package oop.ex6.checker.exceptions;

public abstract class NotDefinedException extends DefinitionException {

    public NotDefinedException(String message) {
        super(message);
    }
}
