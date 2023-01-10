package oop.ex6.checker.methods;

public enum ReturnType {

    VOID("void");


    private final String declarator;
    private final ReturnType[] acceptedTypes;

    ReturnType(String declarator, ReturnType... acceptedTypes) {
        this.declarator = declarator;
        this.acceptedTypes = acceptedTypes;
    }

    public String getDeclarator() {
        return this.declarator;
    }

    public boolean isAccepted(ReturnType variableType) {
        for(ReturnType accepted : this.acceptedTypes) {
            if(accepted == variableType) {
                return true;
            }
        }

        return false;
    }
}
