package oop.ex6.checker.variables;

import oop.ex6.checker.exceptions.DefinitionException;

public class VariableAlreadyDefinedException extends DefinitionException {

    private static final String VARIABLE_ALREADY_DEFINED_MESSAGE = "Variable %s is already defined!";

    public VariableAlreadyDefinedException(String variableName) {
        super(String.format(VARIABLE_ALREADY_DEFINED_MESSAGE, variableName));
    }

    public VariableAlreadyDefinedException(String variableName, Exception exception) {
        super(String.format(VARIABLE_ALREADY_DEFINED_MESSAGE, variableName), exception);
    }
}
