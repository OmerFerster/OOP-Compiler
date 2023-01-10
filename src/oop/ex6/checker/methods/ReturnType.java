package oop.ex6.checker.methods;

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
}
