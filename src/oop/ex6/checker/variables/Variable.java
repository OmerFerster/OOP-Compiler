package oop.ex6.checker.variables;

import oop.ex6.checker.IllegalLineException;

/**
 * A class that represents a single variable object
 */
public class Variable {

    private final VariableType type;

    private final boolean isFinal;

    private boolean isInitialized;

    public Variable(VariableType type, boolean isInitialized, boolean isFinal) {
        this.type = type;

        this.isFinal = isFinal;


        // TODO handle uninitialized variables when entering/leaving method
        this.isInitialized = isInitialized;
    }


    /**
     * @return   The variable's type
     */
    public VariableType getType() {
        return this.type;
    }

    /**
     * @return   Whether the variable is final
     */
    public boolean isFinal() {
        return this.isFinal;
    }

    /**
     * @return   Whether the variable is initialized
     */
    public boolean isInitialized() {
        return this.isInitialized;
    }


    public void initialize(VariableType assigmentType) throws IllegalLineException {
        if(this.isFinal) {
            throw new IllegalLineException("trying to initialize final variable");
            // TODO: throw exception
        }

        if(!this.getType().canAccept(assigmentType)) {
            throw new IllegalLineException("trying to initialize variable with wrong type");
            // TODO: throw exception
        }

        // TODO: if value doesn't match type, throw exception

        this.isInitialized = true;
    }
}
