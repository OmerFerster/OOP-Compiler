package oop.ex6.checker.exceptions;

public abstract class AlreadyDefinedException extends DefinitionException {

    public AlreadyDefinedException(String message) {
        super(message);
    }
}
