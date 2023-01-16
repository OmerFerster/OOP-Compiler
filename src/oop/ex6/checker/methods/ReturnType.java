package oop.ex6.checker.methods;

import oop.ex6.checker.variables.VariableType;

/**
 * An enum representing all allowed return types in sjava
 */
public enum ReturnType {

    VOID("void");


    private final String declarator;

    ReturnType(String declarator) {
        this.declarator = declarator;
    }

    /**
     * Returns the keyword used to reference the return type
     *
     * @return   Declarator keyword
     */
    public String getDeclarator() {
        return this.declarator;
    }


    /**
     * A static method to return the matching ReturnType object from a keyword.
     *
     * @param keyword   Keyword to parse the ReturnType from
     * @return          Matching ReturnType entry
     */
    public static ReturnType fromValue(String keyword) {
        for(ReturnType returnType : ReturnType.values()) {
            if(returnType.getDeclarator().equals(keyword)) {
                return returnType;
            }
        }

        // TODO: throw correct exception
        return null;
    }
}
