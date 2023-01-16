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
        boolean returned = false;

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

            if(returned && lineType != LineType.BLOCK_CLOSE) {
                // todo: throw exception. Not closing
                throw new IllegalLineException("no block close after return");

            } else if (lineType == LineType.METHOD_DECLARATION && scope == 1) {
                String methodName = this.fileParser.getCurrentLine()
                        .split("\\(")[0].split("\\s+")[1];

                Method method = this.methodsTable.getByName(methodName);

                for (Pair<String, Variable> entry : method.getParameters()) {
                    this.variablesTable.addVariable(entry.getKey(),
                            entry.getValue().getType(),
                            entry.getValue().isInitialized(),
                            entry.getValue().isFinal());
                }
            }
            else if (LineParser.isVarDeclarationLine(lineType) && scope >= 1) {
                LineParser.parseVarDeclarationLine(this.variablesTable, this.fileParser.getCurrentLine());
            } else if(lineType == LineType.ASSIGNMENT && scope >= 1) {
                LineParser.parseVarAssignmentLine(this.variablesTable, this.fileParser.getCurrentLine());
            } else if(lineType == LineType.IF && scope >= 1) {
                LineParser.parseConditionalStatement(this.variablesTable, this.fileParser.getCurrentLine());
            } else if(lineType == LineType.WHILE && scope >= 1) {
                LineParser.parseConditionalStatement(this.variablesTable, this.fileParser.getCurrentLine());
            } else if (lineType == LineType.METHOD_CALL && scope >= 1) {
                LineParser.parseMethodCall(this.methodsTable, this.variablesTable, this.fileParser.getCurrentLine());
            } else if(lineType == LineType.RETURN && scope == 1) {
                returned = true;
            } else if(lineType == LineType.BLOCK_CLOSE) {
                if(scope == 0) {
                    if(!returned) {
                        throw new IllegalLineException("no return before block close");
                        // TODO: throw exception - no return
                    } else {
                        returned = false;
                    }

//                    variablesTable.revertGlobalVariables();
                    
                    // Revert all uninitialized variables
                }
            }

            // Compile all lines that are scope >= 1
            // V - check method declaration and add all parameters to variables table of new scope
            // V - check variable declaration in [scope >=1]
            // V - check variable assignment in [scope >=1]
            // V - check if and conditions inside if [scope >=1]
            // V - check while and conditions inside while [scope >=1]
            // V - check method calls and parameters passed [scope >=1]

            // V - check return [scope > 1]
            // V - check return in scope == 1 without } after it
            // V - check } in scope == 1 without return before it

            this.fileParser.advance();
        }
    }
}
