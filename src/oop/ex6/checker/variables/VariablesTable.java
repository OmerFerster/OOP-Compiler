package oop.ex6.checker.variables;

import oop.ex6.checker.ITable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class VariablesTable implements ITable<Variable> {

    private final Map<String, Variable> globalVariables;
    private final LinkedList<Map<String, Variable>> scopeVariables;

    public VariablesTable() {
        this.globalVariables = new HashMap<>();
        this.scopeVariables = new LinkedList<>();
    }


    public void openScope() {
        Map<String, Variable> scope = new HashMap<>();

        this.scopeVariables.add(scope);
    }

    public void closeScope() {
        if (this.scopeVariables.size() == 0) {
            // TODO: might wanna throw an exception
            return;
        }

        this.scopeVariables.removeLast();
    }


    public Variable addVariable(String name, VariableType variableType, boolean initialized,
                                boolean isFinal) throws VariableAlreadyDefinedException {
        Variable variable = new Variable(variableType, initialized, isFinal);

        boolean succeeded;

        if (this.scopeVariables.size() == 0) {
            succeeded = this.addGlobalVariable(name, variable);
        } else {
            succeeded = this.addLocalVariable(name, variable);
        }

        if(!succeeded) {
            throw new VariableAlreadyDefinedException(name);
        }

        return variable;
    }

    private boolean addGlobalVariable(String name, Variable variable) {
        if (this.globalVariables.containsKey(name)) {
            return false;
        }

        this.globalVariables.put(name, variable);
        return true;
    }

    private boolean addLocalVariable(String name, Variable variable) {
        if (this.scopeVariables.getLast().containsKey(name)) {
            return false;
        }

        this.scopeVariables.getLast().put(name, variable);
        return true;
    }


    public boolean exists(String name) {
        return this.getByName(name) != null;
    }

    public Variable getByName(String name) {
        if (this.scopeVariables.size() > 0) {
            Iterator<Map<String, Variable>> scopeIterator = this.scopeVariables.descendingIterator();

            Map<String, Variable> currentScope;

            while (scopeIterator.hasNext()) {
                currentScope = scopeIterator.next();

                if (currentScope.containsKey(name)) {
                    return currentScope.get(name);
                }
            }
        }

        if (this.globalVariables.containsKey(name)) {
            return this.globalVariables.get(name);
        }

        // TODO: might wanna throw a "not exists exception"
        return null;
    }
}