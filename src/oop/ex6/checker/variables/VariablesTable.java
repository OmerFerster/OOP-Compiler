package oop.ex6.checker.variables;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class VariablesTable {

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


    public void addVariable(String name, VariableType type, boolean initialized, boolean isFinal) {
        Variable variable = new Variable(type, initialized, isFinal);

        // TODO: if failed might wanna throw an exception
        if (this.scopeVariables.size() == 0) {
            this.addGlobalVariable(name, variable);
        } else {
            this.addLocalVariable(name, variable);
        }
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


    public Variable getVariableByName(String name) {
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

// TODO Keep track of global variables initialized inside the current function