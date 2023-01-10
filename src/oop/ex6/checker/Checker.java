package oop.ex6.checker;

import oop.ex6.checker.methods.MethodsTable;
import oop.ex6.checker.variables.VariablesTable;
import oop.ex6.parser.FileParser;

public class Checker {

    private final FileParser fileParser;

    private final VariablesTable variablesTable;
    private final MethodsTable methodsTable;


    public Checker(FileParser fileParser) {
        this.fileParser = fileParser;

        this.variablesTable = new VariablesTable();
        this.methodsTable = new MethodsTable();

        this.initTables();
    }


    private void initTables() {
        int scope = 0;

        while(this.fileParser.hasMoreLines()) {
//            LineType lineType = LineType.getLineType(this.fileParser.getCurrentLine());

            // Compilable  [compile(line)]
            // - VarDecCompiler
            // - VarAssignCompiler
            // -

//            if (LineType.isVarDeclarationLine(lineType) && scope == 0) {
//
//            }

            // if var dec (in global scope): add to symbol table
            // if entering new scope: remember it
            // if leaving scope: remember it
            // if initializing global var, update symbol table

            this.fileParser.advance();
        }

        this.fileParser.reset();
    }


    public void check() throws CheckerException {
        this.fileParser.reset();

        try {
            compileFile();

        } catch (CheckerException exception) {
            throw exception;
        }
    }

    public void compileFile() throws CheckerException {

    }
}
