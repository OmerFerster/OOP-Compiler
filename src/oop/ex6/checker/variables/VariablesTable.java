package oop.ex6.checker.variables;

import oop.ex6.checker.ITable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * A class used to store all variables used within a sjava program
 */
public class VariablesTable implements ITable<Variable> {

    private final Map<String, Variable> globalVariables;
    private final LinkedList<Map<String, Variable>> scopeVariables;

    public VariablesTable() {
        this.globalVariables = new HashMap<>();
        this.scopeVariables = new LinkedList<>();
    }


    /**
     * Opens a new scope for local variables
     */
    public void openScope() {
        Map<String, Variable> scope = new HashMap<>();

        this.scopeVariables.add(scope);
    }

    /**
     * Closes the latest scope
     */
    public void closeScope() {
        if (this.scopeVariables.size() == 0) {
            // TODO: might wanna throw an exception
            return;
        }

        this.scopeVariables.removeLast();
    }


    /**
     * Adds a new variable into the variables table
     *
     * @param name                               Name of the variable
     * @param variableType                       Type of the variable
     * @param initialized                        Whether the variable is initialized
     * @param isFinal                            Whether the variable is final
     * @return                                   Created Variable object
     * @throws VariableAlreadyDefinedException   Throws an exception if variable with the same name
     *                                           already exists within the same scope
     */
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

    /**
     * Tries to add a given variable into the global variables table
     *
     * @param name       Name of the variable to add table
     * @param variable   Variable object to add to the table
     * @return           Whether the insertion was successful
     */
    private boolean addGlobalVariable(String name, Variable variable) {
        if (this.globalVariables.containsKey(name)) {
            return false;
        }

        this.globalVariables.put(name, variable);
        return true;
    }

    /**
     * Tries to add a given variable into the latest local variables table
     *
     * @param name       Name of the variable to add table
     * @param variable   Variable object to add to the table
     * @return           Whether the insertion was successful
     */
    private boolean addLocalVariable(String name, Variable variable) {
        if (this.scopeVariables.getLast().containsKey(name)) {
            return false;
        }

        this.scopeVariables.getLast().put(name, variable);
        return true;
    }


    /**
     * Returns whether a variable with the given name exists in any scope
     *
     * @param name   Name of the variable to check
     * @return       Whether a variable of that name exists
     */
    @Override
    public boolean exists(String name) {
        return this.getByName(name) != null;
    }

    /**
     * Returns the Variable object matches to the given name, from the closest scope.
     * If no such object exists, it returns null
     *
     * @param name   Name of the variable to return
     * @return       Variable object matched to the given name
     */
    @Override
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