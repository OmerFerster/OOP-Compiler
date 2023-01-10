package oop.ex6.checker.methods;

import oop.ex6.checker.ITable;
import oop.ex6.checker.variables.VariableType;

import java.util.*;

public class MethodsTable implements ITable<Method> {

    private final Map<String, Method> methods;

    public MethodsTable() {
        this.methods = new HashMap<>();
    }


    public void addMethod(String name, ReturnType returnType, Map<String, VariableType> parameters) {
        if (this.methods.containsKey(name)) {
            // TODO: might wanna throw an exception
            return;
        }

        Method method = new Method(returnType, parameters);

        this.methods.put(name, method);
    }

    public boolean exists(String name) {
        return this.getByName(name) != null;
    }

    public Method getByName(String name) {
        if (this.methods.containsKey(name)) {
            return this.methods.get(name);
        }

        // TODO: might wanna throw a "not exists exception"
        return null;
    }
}