package oop.ex6.checker.variables;

public enum VariableType {

    INT("int"),
    DOUBLE("double", INT),
    STRING("String"),
    BOOLEAN("boolean", INT, DOUBLE),
    CHAR("char");


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
}
