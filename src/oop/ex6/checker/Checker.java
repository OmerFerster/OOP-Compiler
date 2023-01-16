package oop.ex6.checker;

import oop.ex6.checker.methods.MethodsTable;
import oop.ex6.checker.methods.ReturnType;
import oop.ex6.checker.variables.VariablesTable;
import oop.ex6.parser.FileParser;

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
            } else if (LineParser.isVarAssignmentLine(lineType) && scope == 0) {
                LineParser.parseVarAssignmentLine(this.variablesTable, this.fileParser.getCurrentLine());
            } else if (lineType == LineType.METHOD_DECLARATION && scope == 0) {
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


            if (lineType == LineType.METHOD_DECLARATION) {
//                String[] tokens = this.fileParser.getCurrentLine().split("\\(")[0].split("\\s*")[2];
//
//                String methodName = tokens[0].split(" ")[1];
                // void foo() {
                // methodTable.getByName(foo)
            }
            else if (LineParser.isVarDeclarationLine(lineType) && scope >= 1) {
                LineParser.parseVarDeclarationLine(this.variablesTable, this.fileParser.getCurrentLine());
            }
            else if(LineParser.isVarAssignmentLine(lineType) && scope >= 1) {
                LineParser.parseVarAssignmentLine(this.variablesTable, this.fileParser.getCurrentLine());
            }

            // Compile all lines that are scope >= 1
            //   - check method declaration and add all parameters to variables table of new scope
            // - check variable declaration in [scope >=1]
            // - check variable assignment in [scope >=1]
            // - check if and conditions inside if [scope >=1]
            // - check while and conditions inside while [scope >=1]
            // - check method calls and parameters passed [scope >=1]
            // - check return [scope > 1]
            // - check return in scope == 1 without } after it
            // - check } in scope == 1 without return before it



            this.fileParser.advance();
        }
    }
}
