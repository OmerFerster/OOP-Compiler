package oop.ex6.checker.methods;

import oop.ex6.checker.variables.Variable;
import oop.ex6.checker.variables.VariableType;
import oop.ex6.utils.Pair;

import java.util.List;
import java.util.Map;

/**
 * A class that represents a single method object
 */
public class Method {

    private final ReturnType returnType;

    private final List<Pair<String, Variable>> parameters;

    public Method(ReturnType returnType, List<Pair<String, Variable>> parameters) {
        this.returnType = returnType;

        this.parameters = parameters;
    }


    /**
     * @return   The method's return type
     */
    public ReturnType getReturnType() {
        return this.returnType;
    }

    /**
     * @return   The method's parameter list with their respected name
     */
    public List<Pair<String, Variable>> getParameters() {
        return this.parameters;
    }
}
