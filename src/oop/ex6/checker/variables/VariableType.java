package oop.ex6.checker.variables;

import oop.ex6.utils.Constants;

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

    public String getDeclarator() {
        return this.declarator;
    }

    public boolean isAccepted(VariableType variableType) {
        for(VariableType accepted : this.acceptedTypes) {
            if(accepted == variableType) {
                return true;
            }
        }

        return false;
    }


    public static VariableType fromValue(String keyword) {
        switch (keyword) {
            case Constants.INT_KEYWORD:
                return INT;
            case Constants.DOUBLE_KEYWORD:
                return DOUBLE;
            case Constants.STRING_KEYWORD:
                return STRING;
            case Constants.BOOLEAN_KEYWORD:
                return BOOLEAN;
            case Constants.CHAR_KEYWORD:
                return CHAR;
        }

        return null;
    }
}
