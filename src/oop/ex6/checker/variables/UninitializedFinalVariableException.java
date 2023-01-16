package oop.ex6.checker.variables;

import oop.ex6.checker.exceptions.DefinitionException;

public class UninitializedFinalVariableException extends DefinitionException {

    private static final String UNINITIALIZED_FINAL_VARIABLE_MESSAGE = "Variable %s is uninitialized final!";

    public UninitializedFinalVariableException(String variableName) {
        super(String.format(UNINITIALIZED_FINAL_VARIABLE_MESSAGE, variableName));
    }

    public UninitializedFinalVariableException(String variableName, Exception exception) {
        super(String.format(UNINITIALIZED_FINAL_VARIABLE_MESSAGE, variableName), exception);
    }
}
