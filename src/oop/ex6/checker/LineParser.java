package oop.ex6.checker;

import oop.ex6.checker.exceptions.AlreadyDefinedException;
import oop.ex6.checker.variables.Variable;
import oop.ex6.checker.variables.VariableAlreadyDefinedException;
import oop.ex6.checker.variables.VariableType;
import oop.ex6.checker.variables.VariablesTable;
import oop.ex6.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class LineParser {

    private final static char COMMA = ',';
    private final static char SPACE = ' ';

    /**
     * Parses a single line into a list of created variables by reading the line's data.
     * It assumes line is indeed a VarDeclaration line.
     * @see #isVarDeclarationLine
     *
     * @param variables   An ITable representing all created variables so far
     * @param line        The line to parse
     * @return            Created variables
     */
    public static List<Variable> parseVarDeclarationLine(VariablesTable variables, String line)
            throws VariableAlreadyDefinedException {
        // Splits the line into all tokens, removing unnecessary spaces and commas
        String[] tokens = line.replaceAll("\\s*=\\s*", "=")
                .split("(\\s*,\\s*|\\s+)");

        boolean isFinal = tokens[0].equals(Constants.FINAL_KEYWORD);

        VariableType variableType = VariableType.fromValue(isFinal ? tokens[1] : tokens[0]);
        // TODO: throw parse excpetion if variableType == null

        List<Variable> declaredVariables = new ArrayList<>();

        // Loops over all declarations, except for (final) [type] and ';'
        for (int i = (isFinal ? 2 : 1); i < tokens.length - 1; i++) {
            String token = tokens[i];

            boolean isInitialized = token.contains("=");
            String variableName = token.split("=")[0];

            declaredVariables.add(variables.addVariable(variableName, variableType,
                    isInitialized, isFinal));
        }

        return declaredVariables;
    }



    public static LineType getLineType(String line) throws IllegalLineTypeException {
        for (LineType lineType : LineType.values()) {
            Matcher matcher = lineType.getPattern().matcher(line);

            if (matcher.matches()) {
                return lineType;
            }
        }

        throw new IllegalLineTypeException(line);
    }


    public static boolean isVarDeclarationLine(LineType lineType) {
        return lineType == LineType.INT_VAR_DECLARATION ||
                lineType == LineType.DOUBLE_VAR_DECLARATION ||
                lineType == LineType.CHAR_VAR_DECLARATION ||
                lineType == LineType.BOOLEAN_VAR_DECLARATION ||
                lineType == LineType.STRING_VAR_DECLARATION;
    }

    public static boolean isVarAssignmentLine(LineType lineType) {
        return lineType == LineType.IDENTIFIER_VAR_ASSIGNMENT ||
                lineType == LineType.INT_VAR_ASSIGMENT ||
                lineType == LineType.DOUBLE_VAR_ASSIGMENT ||
                lineType == LineType.CHAR_VAR_ASSIGMENT ||
                lineType == LineType.BOOLEAN_VAR_ASSIGMENT ||
                lineType == LineType.STRING_VAR_ASSIGMENT;
    }

    public static boolean isBlockOpener(LineType lineType) {
        return lineType == LineType.IF ||
                lineType == LineType.WHILE ||
                lineType == LineType.METHOD_DECLARATION;
    }

    public static boolean isBlockCloser(LineType lineType) {
        return lineType == LineType.BLOCK_CLOSE;
    }

    public static boolean isIgnoredLine(LineType lineType) {
        return lineType == LineType.EMPTY_LINE ||
                lineType == LineType.COMMENT_LINE;
    }
}
