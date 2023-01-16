package oop.ex6.checker.methods;

import oop.ex6.checker.ITable;
import oop.ex6.checker.variables.Variable;
import oop.ex6.checker.variables.VariableType;

import java.util.*;

/**
 * A class used to store all methods used within a sjava program
 */
public class MethodsTable implements ITable<Method> {

    private final Map<String, Method> methods;

    public MethodsTable() {
        this.methods = new HashMap<>();
    }


    /**
     * Adds a new method into the methods table
     *
     * @param name         Name of the method
     * @param returnType   Return type of the method
     * @param parameters   A list of parameters the method requires
     * @return             Created Method object
     */
    public Method addMethod(String name, ReturnType returnType, Map<String, Variable> parameters) {
        if (this.methods.containsKey(name)) {
            // TODO: might wanna throw an exception
            return null;
        }

        Method method = new Method(returnType, parameters);

        this.methods.put(name, method);

        return method;
    }

    /**
     * Returns whether a method with the given name exists
     *
     * @param name   Name of the method to check
     * @return       Whether a method of that name exists
     */
    @Override
    public boolean exists(String name) {
        return this.getByName(name) != null;
    }

    /**
     * Returns the Method object matches to the given name
     * If no such object exists, it returns null
     *
     * @param name   Name of the method to return
     * @return       Method object matched to the given name
     */
    @Override
    public Method getByName(String name) {
        if (this.methods.containsKey(name)) {
            return this.methods.get(name);
        }

        // TODO: might wanna throw a "not exists exception"
        return null;
    }
}