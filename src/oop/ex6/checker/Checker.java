package oop.ex6.checker;

import oop.ex6.checker.methods.Method;
import oop.ex6.checker.methods.MethodsTable;
import oop.ex6.checker.variables.Variable;
import oop.ex6.checker.variables.VariablesTable;
import oop.ex6.parser.FileParser;
import oop.ex6.utils.Pair;

public class Checker {

    private final FileParser fileParser;

    private final VariablesTable variablesTable;
    private final MethodsTable methodsTable;

    public Checker(FileParser fileParser) throws CheckerException {
        this.fileParser = fileParser;

        this.variablesTable = new VariablesTable();
        this.methodsTable = new MethodsTable();
    }


    private void initTables() throws CheckerException {
        int scope = 0;

        while (this.fileParser.hasMoreLines()) {
            LineType lineType = LineParser.getLineType(this.fileParser.getCurrentLine());

            // Handle scoping
            if (LineParser.isBlockOpener(lineType)) {
                this.variablesTable.openScope();
                scope++;
            } else if (LineParser.isBlockCloser(lineType)) {
                this.variablesTable.closeScope();
                scope--;
            }

            if (LineParser.isVarDeclarationLine(lineType) && scope == 0) {
                LineParser.parseVarDeclarationLine(this.variablesTable, this.fileParser.getCurrentLine());
            } else if (lineType == LineType.ASSIGNMENT && scope == 0) {
                LineParser.parseVarAssignmentLine(this.variablesTable, this.fileParser.getCurrentLine());
            } else if (lineType == LineType.METHOD_DECLARATION && scope == 1) {
                LineParser.parseMethodDeclarationLine(this.methodsTable, this.fileParser.getCurrentLine());
            }

            this.fileParser.advance();
        }

        for (Variable globalVariable : this.variablesTable.getGlobalVariable()) {
            if (globalVariable.isInitialized()) {
                globalVariable.setFirstInitialization(true);
            }
        }
    }


    public void check() throws CheckerException {
        try {
            this.fileParser.reset();
            this.initTables();

            this.fileParser.reset();
            this.compileFile();
        } catch (CheckerException exception) {
            throw new IllegalLineException(exception.getMessage(), exception);
        }
    }

    public void compileFile() throws CheckerException {
        int scope = 0;

        while (this.fileParser.hasMoreLines()) {
            LineType lineType = LineParser.getLineType(this.fileParser.getCurrentLine());

            // Handle scoping
            if (LineParser.isBlockOpener(lineType)) {
                this.variablesTable.openScope();
                scope++;
            } else if (LineParser.isBlockCloser(lineType)) {
                this.variablesTable.closeScope();
                scope--;
            }

            if (lineType == LineType.METHOD_DECLARATION && scope == 1) {
                String methodName = this.fileParser.getCurrentLine()
                        .split("\\(")[0].split("\\s+")[1];

                Method method = this.methodsTable.getByName(methodName);

                for (Pair<String, Variable> entry : method.getParameters()) {
                    this.variablesTable.addVariable(entry.getKey(),
                            entry.getValue().getType(),
                            entry.getValue().isInitialized(),
                            entry.getValue().isFinal());
                }
            } else if (LineParser.isVarDeclarationLine(lineType) && scope >= 1) {
                LineParser.parseVarDeclarationLine(this.variablesTable, this.fileParser.getCurrentLine());
            } else if (lineType == LineType.ASSIGNMENT && scope >= 1) {
                LineParser.parseVarAssignmentLine(this.variablesTable, this.fileParser.getCurrentLine());
            } else if (lineType == LineType.IF && scope >= 1) {
                LineParser.parseConditionalStatement(this.variablesTable, this.fileParser.getCurrentLine());
            } else if (lineType == LineType.WHILE && scope >= 1) {
                LineParser.parseConditionalStatement(this.variablesTable, this.fileParser.getCurrentLine());
            } else if (lineType == LineType.METHOD_CALL && scope >= 0) { // TODO check scope maybe 0
                LineParser.parseMethodCall(this.methodsTable, this.variablesTable, this.fileParser.getCurrentLine());
            } else if (lineType == LineType.BLOCK_CLOSE && scope == 0) {
                LineType prevLineType = LineParser.getLineType(this.fileParser.getPreviousLine());
                if (!(prevLineType == LineType.RETURN)) {
                    throw new IllegalLineException("no return before block close");
                }

                this.variablesTable.revertGlobalVariables(); // Revert all uninitialized variables
            }

            this.fileParser.advance();
        }

        if (scope != 0) {  // checking if scopes closed the way it should;
            throw new IllegalLineException("missing closing parentheses");
        }
    }
}
