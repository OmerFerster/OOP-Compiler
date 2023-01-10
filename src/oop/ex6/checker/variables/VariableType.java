package oop.ex6.checker.variables;

import oop.ex6.utils.Constants;

/**
 * An enum representing all allowed variable types in sjava
 */
public enum VariableType {

    INT(Constants.INT_KEYWORD),
    DOUBLE(Constants.DOUBLE_KEYWORD, INT),
    STRING(Constants.STRING_KEYWORD),
    BOOLEAN(Constants.BOOLEAN_KEYWORD, INT, DOUBLE),
    CHAR(Constants.CHAR_KEYWORD);


    private final String declarator;
    private final VariableType[] acceptedTypes;

    VariableType(String declarator, VariableType... acceptedTypes) {
        this.declarator = declarator;
        this.acceptedTypes = acceptedTypes;
    }

    /**
     * Returns the keyword used to reference the variable type
     *
     * @return   Declarator keyword
     */
    public String getDeclarator() {
        return this.declarator;
    }

    /**
     * Returns whether a given type can be accepted for the current type
     *
     * @param variableType   Type to check acceptance
     * @return               Whether the given type can be accepted
     */
    public boolean isAccepted(VariableType variableType) {
        for(VariableType accepted : this.acceptedTypes) {
            if(accepted == variableType) {
                return true;
            }
        }

        return false;
    }


    /**
     * A static method to return the matching VariableType object from a keyword.
     *
     * @param keyword   Keyword to parse the VariableType from
     * @return          Matching VariableType entry
     */
    public static VariableType fromValue(String keyword) {
        for(VariableType variableType : VariableType.values()) {
            if(variableType.getDeclarator().equals(keyword)) {
                return variableType;
            }
        }

        return null;
    }
}
