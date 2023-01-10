import oop.ex6.checker.variables.VariablesTable;
import oop.ex6.checker.variables.VariableType;

public class VariableTableTest {

    public static void main(String[] args) {
        VariablesTable symbolTable = new VariablesTable();

        symbolTable.addVariable("a", VariableType.BOOLEAN, false, false);
        symbolTable.addVariable("a", VariableType.INT, false, false);

        System.out.println(symbolTable.getByName("a").getType() == VariableType.BOOLEAN);

        symbolTable.openScope();
        symbolTable.addVariable("a", VariableType.CHAR, false, false);
        System.out.println(symbolTable.getByName("a").getType() == VariableType.CHAR);

        symbolTable.openScope();
        symbolTable.addVariable("a", VariableType.DOUBLE, false, false);
        System.out.println(symbolTable.getByName("a").getType() == VariableType.DOUBLE);

        symbolTable.closeScope();
        System.out.println(symbolTable.getByName("a").getType() == VariableType.CHAR);

        symbolTable.closeScope();
        System.out.println(symbolTable.getByName("a").getType() == VariableType.BOOLEAN);
    }
}
