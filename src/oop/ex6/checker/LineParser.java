package oop.ex6.checker;

import oop.ex6.checker.variables.Variable;
import oop.ex6.checker.variables.VariableAlreadyDefinedException;
import oop.ex6.checker.variables.VariableType;
import oop.ex6.checker.variables.VariablesTable;
import oop.ex6.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class LineParser {

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
        // TODO: throw parse exception if variableType == null

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


    /**
     * Returns the type of the given line
     *
     * @param line                    Line to parse the type of
     * @return                        Given line's type
     * @throws IllegalLineException   Throws an IllegalLineTypeException if no valid LineType found,
     *                                meaning the given line is an illegal line
     */
    public static LineType getLineType(String line) throws IllegalLineException {
        for (LineType lineType : LineType.values()) {
            Matcher matcher = lineType.getPattern().matcher(line);

            if (matcher.matches()) {
                return lineType;
            }
        }

        throw new IllegalLineException(line);
    }


    /**
     * Returns whether a line type is a variable declaration line
     *
     * @param lineType   Line type to check
     * @return           Whether the given line is a variable declaration
     */
    public static boolean isVarDeclarationLine(LineType lineType) {
        return lineType == LineType.INT_VAR_DECLARATION ||
                lineType == LineType.DOUBLE_VAR_DECLARATION ||
                lineType == LineType.CHAR_VAR_DECLARATION ||
                lineType == LineType.BOOLEAN_VAR_DECLARATION ||
                lineType == LineType.STRING_VAR_DECLARATION;
    }

    /**
     * Returns whether a line type is a variable assignment line
     *
     * @param lineType   Line type to check
     * @return           Whether the given line is a variable assignment
     */
    public static boolean isVarAssignmentLine(LineType lineType) {
        return lineType == LineType.IDENTIFIER_VAR_ASSIGNMENT ||
                lineType == LineType.INT_VAR_ASSIGMENT ||
                lineType == LineType.DOUBLE_VAR_ASSIGMENT ||
                lineType == LineType.CHAR_VAR_ASSIGMENT ||
                lineType == LineType.BOOLEAN_VAR_ASSIGMENT ||
                lineType == LineType.STRING_VAR_ASSIGMENT;
    }

    /**
     * Returns whether a line type is a block opener line
     *
     * @param lineType   Line type to check
     * @return           Whether the given line is a block opener
     */
    public static boolean isBlockOpener(LineType lineType) {
        return lineType == LineType.IF ||
                lineType == LineType.WHILE ||
                lineType == LineType.METHOD_DECLARATION;
    }

    /**
     * Returns whether a line type is a block closer line
     *
     * @param lineType   Line type to check
     * @return           Whether the given line is a block closer
     */
    public static boolean isBlockCloser(LineType lineType) {
        return lineType == LineType.BLOCK_CLOSE;
    }

    /**
     * Returns whether a line type is an ignored line
     *
     * @param lineType   Line type to check
     * @return           Whether the given line is an ignored line
     */
    public static boolean isIgnoredLine(LineType lineType) {
        return lineType == LineType.EMPTY_LINE ||
                lineType == LineType.COMMENT_LINE;
    }
}
