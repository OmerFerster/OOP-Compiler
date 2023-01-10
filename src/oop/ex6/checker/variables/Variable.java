package oop.ex6.checker.variables;

public class Variable {

    private final VariableType type;

    private final boolean isInitialized;
    private final boolean isFinal;

    public Variable(VariableType type, boolean isInitialized, boolean isFinal) {
        this.type = type;

        this.isInitialized = isInitialized;
        this.isFinal = isFinal;
    }


    public VariableType getType() {
        return this.type;
    }

    public boolean isInitialized() {
        return this.isInitialized;
    }

    public boolean isFinal() {
        return this.isFinal;
    }
}
