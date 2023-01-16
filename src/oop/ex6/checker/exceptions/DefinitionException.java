package oop.ex6.checker.exceptions;

import oop.ex6.checker.CheckerException;

public abstract class DefinitionException extends CheckerException {

    public DefinitionException(String message) {
        super(message);
    }

    public DefinitionException(String message, Exception exception) {
        super(message, exception);
    }
}

