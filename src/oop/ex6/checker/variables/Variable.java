package oop.ex6.checker.variables;

/**
 * A class that represents a single variable object
 */
public class Variable {

    private final VariableType type;

    private final boolean isInitialized;
    private final boolean isFinal;

    public Variable(VariableType type, boolean isInitialized, boolean isFinal) {
        this.type = type;

        this.isInitialized = isInitialized;
        this.isFinal = isFinal;
    }


    /**
     * @return   The variable's type
     */
    public VariableType getType() {
        return this.type;
    }

    /**
     * @return   Whether the variable is initialized
     */
    public boolean isInitialized() {
        return this.isInitialized;
    }

    /**
     * @return   Whether the variable is final
     */
    public boolean isFinal() {
        return this.isFinal;
    }
}
