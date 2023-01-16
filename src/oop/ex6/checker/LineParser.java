package oop.ex6.checker;

import oop.ex6.checker.exceptions.DefinitionException;
import oop.ex6.checker.methods.Method;
import oop.ex6.checker.methods.MethodsTable;
import oop.ex6.checker.methods.ReturnType;
import oop.ex6.checker.variables.*;
import oop.ex6.utils.Constants;
import oop.ex6.utils.RegexConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineParser {

    // TODO:
    // make line parser non-static

    /**
     * Parses a single line into a list of created variables by reading the line's data.
     * It assumes line is indeed a VarDeclaration line.
     *
     * @param variables An ITable representing all created variables so far
     * @param line      The line to parse
     * @return Created variables
     * @see #isVarDeclarationLine
     */
    public static List<Variable> parseVarDeclarationLine(VariablesTable variables, String line)
            throws IllegalLineException, DefinitionException {

        // Splits the line into all tokens, removing unnecessary spaces, commas and ;
        String[] tokens = line.replaceAll("\\s*=\\s*", "=")
                .split("(\\s*,\\s*|\\s+|\\s*;\\s*)");



        boolean isFinal = tokens[0].equals(Constants.FINAL_KEYWORD);

        VariableType variableType = VariableType.fromValue(isFinal ? tokens[1] : tokens[0]);
        // TODO: throw parse exception if variableType == null

        List<Variable> declaredVariables = new ArrayList<>();

        // Loops over all declarations, except for (final) [type] and ';'
        for (int i = (isFinal ? 2 : 1); i < tokens.length; i++) {
            String token = tokens[i];

            // TODO: create variable object and use #variable.initialize instead of checking by hand
            // the 2nd exception

            boolean isInitialized = token.contains("=");

            if(isInitialized) {
                if(getAssignmentType(token.split("=")[1], variables) == VariableType.IDENTIFIER) {
                    if(variables.getByName(token.split("=")[1]).getType() != variableType) {
                        // TODO: throw exception
                        throw new IllegalLineException("Tried to initialize variable with wrong type!");
                    }
                    if(!variables.getByName(token.split("=")[1]).isInitialized()) {
                        // TODO: throw exception
                        throw new IllegalLineException("Tried to initialize variable with wrong type!");
                    }
                }
            }

            String variableName = token.split("=")[0];

            declaredVariables.add(variables.addVariable(variableName, variableType,
                    isInitialized, isFinal));
        }

        return declaredVariables;
    }

    public static Method parseMethodDeclarationLine(MethodsTable methods, String line) {
        // Splits the line into all tokens, removing unnecessary spaces, commas and ;
        String[] tokens = line.replaceAll("\\s+", " ")
                .split("(\\s*,\\s*|\\s*\\(\\s*|\\s*\\)\\s*\\{\\s*)");

        ReturnType returnType = ReturnType.fromValue(tokens[0].split(" ")[0]);

        String methodName = tokens[0].split(" ")[1];

        Map<String, Variable> parameters = new HashMap<>();

        for (int i = 1; i < tokens.length; i++) {
            String[] variableData = tokens[i].split(" ");

            boolean isFinal;
            VariableType variableType;
            String variableName;

            if (variableData[0].equals(Constants.FINAL_KEYWORD)) {
                isFinal = true;
                variableType = VariableType.fromValue(variableData[1]);
                variableName = variableData[2];
            } else {
                isFinal = false;
                variableType = VariableType.fromValue(variableData[0]);
                variableName = variableData[1];
            }

            parameters.put(variableName, new Variable(variableType, true, isFinal));
        }

        return methods.addMethod(methodName, returnType, parameters);
    }

    public static void parseVarAssignmentLine(VariablesTable variables, String line) throws IllegalLineException {
        // Splits the line into all tokens, removing unnecessary spaces, commas and ;
        String[] tokens = line.replaceAll("\\s+", "").split("[=;]");

        Variable variable = variables.getByName(tokens[0]);

        VariableType assignmentType = getAssignmentType(tokens[1], variables);
        if(assignmentType == VariableType.IDENTIFIER) {
            if(!variables.getByName(tokens[1]).isInitialized()) {
                // todo: throw exception
            }

            assignmentType = variables.getByName(tokens[1]).getType();
        }

        variable.initialize(assignmentType);
    }


    private static Pattern intValuePattern = Pattern.compile(RegexConstants.INT_VALUE);
    private static Pattern doubleValuePattern = Pattern.compile(RegexConstants.DOUBLE_VALUE);
    private static Pattern charValuePattern = Pattern.compile(RegexConstants.CHAR_VALUE);
    private static Pattern stringValuePattern = Pattern.compile(RegexConstants.STRING_VALUE);
    private static Pattern booleanValuePattern = Pattern.compile(RegexConstants.BOOLEAN_VALUE);
    private static Pattern identifierValuePattern = Pattern.compile(RegexConstants.IDENTIFIER);

    public static VariableType getAssignmentType(String assignment, VariablesTable table) {
        if (intValuePattern.matcher(assignment).matches()){
            return VariableType.INT;
        } else if (doubleValuePattern.matcher(assignment).matches()){
            return VariableType.DOUBLE;
        } else if (charValuePattern.matcher(assignment).matches()){
            return VariableType.CHAR;
        } else if (stringValuePattern.matcher(assignment).matches()){
            return VariableType.STRING;
        } else if (booleanValuePattern.matcher(assignment).matches()){
            return VariableType.BOOLEAN;
        } else if (identifierValuePattern.matcher(assignment).matches()){
            return VariableType.IDENTIFIER;
        }

        // TODO: throw an exception
        return null;
    }




    /**
     * Returns the type of the given line
     *
     * @param line Line to parse the type of
     * @return Given line's type
     * @throws IllegalLineException Throws a CheckerException if no valid LineType found,
     *                              meaning the given line is an illegal line
     */
    public static LineType getLineType(String line) throws IllegalLineException {
        for (LineType lineType : LineType.values()) {
            Matcher matcher = lineType.getPattern().matcher(line);

            if (matcher.matches()) {
                return lineType;
            }
        }

        throw new IllegalLineException(String.format(Constants.ILLEGAL_LINE_MESSAGE, line));
    }

    /**
     * Returns whether a line type is a variable declaration line
     *
     * @param lineType Line type to check
     * @return Whether the given line is a variable declaration
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
     * @param lineType Line type to check
     * @return Whether the given line is a variable assignment
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
     * @param lineType Line type to check
     * @return Whether the given line is a block opener
     */
    public static boolean isBlockOpener(LineType lineType) {
        return lineType == LineType.IF ||
                lineType == LineType.WHILE ||
                lineType == LineType.METHOD_DECLARATION;
    }

    /**
     * Returns whether a line type is a block closer line
     *
     * @param lineType Line type to check
     * @return Whether the given line is a block closer
     */
    public static boolean isBlockCloser(LineType lineType) {
        return lineType == LineType.BLOCK_CLOSE;
    }

    /**
     * Returns whether a line type is an ignored line
     *
     * @param lineType Line type to check
     * @return Whether the given line is an ignored line
     */
    public static boolean isIgnoredLine(LineType lineType) {
        return lineType == LineType.EMPTY_LINE ||
                lineType == LineType.COMMENT_LINE;
    }
}
