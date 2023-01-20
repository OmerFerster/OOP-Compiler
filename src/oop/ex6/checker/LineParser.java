package oop.ex6.checker;

import oop.ex6.checker.exceptions.DefinitionException;
import oop.ex6.checker.methods.Method;
import oop.ex6.checker.methods.MethodsTable;
import oop.ex6.checker.methods.ReturnType;
import oop.ex6.checker.variables.*;
import oop.ex6.utils.Constants;
import oop.ex6.utils.Pair;
import oop.ex6.utils.RegexConstants;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineParser {

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

        // Splits the line into all equations, while removing commas between equations
        // Example: "final  int  a = b, c = -5, e = 5"   ->  [final  int  a=b, c="1 2", e=5]
        String[] tokens = line.replaceAll("\\s*=\\s*", "=")
                .split("(\\s*,\\s*|\\s*;\\s*)");

        // Checking if the declaration is final & saving its type
        String[] data = tokens[0].split("\\s+");

        boolean isFinal = data[0].equals(Constants.FINAL_KEYWORD);
        VariableType variableType = VariableType.fromValue(isFinal ? data[1] : data[0]);
        // TODO: throw parse exception if variableType == null

        // "Fixing" first token - removing (final) and <type>
        tokens[0] = tokens[0].replaceAll(data[0], "");
        tokens[0] = isFinal ? tokens[0].replaceAll(data[1], "") : tokens[0];
        tokens[0] = tokens[0].strip();


        List<Variable> declaredVariables = new ArrayList<>();

        // Loops over all declarations, except for (final) [type] and ';'
        for (String token : tokens) {
            // TODO: create variable object and use #variable.initialize instead of checking by hand
            // the 2nd exception

            boolean isInitialized = token.contains("=");

            if (isInitialized) {
                if (getExpressionType(token.split("=")[1], variables) == VariableType.IDENTIFIER) {
                    if (!variableType.canAccept(variables.getByName(token.split("=")[1]).getType())) {
                        // TODO: throw exception
                        throw new IllegalLineException("Tried to initialize variable with wrong type!");
                    }
                    if (!variables.getByName(token.split("=")[1]).isInitialized()) {
                        // TODO: throw exception
                        throw new IllegalLineException("Tried to initialize variable with uninitialized variable!");
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

        List<Pair<String, Variable>> parameters = new ArrayList<>();

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

            parameters.add(new Pair<>(variableName, new Variable(variableType, true, isFinal)));
        }

        return methods.addMethod(methodName, returnType, parameters);
    }

    public static void parseVarAssignmentLine(VariablesTable variables,
                                              String line) throws IllegalLineException {

        // Splits the line into all equations, while removing commas between equations
        String[] tokens = line.replaceAll("\\s*=\\s*", "=")
                .split("(\\s*,\\s*|;)");

        // Loops over every equation and initialize correct variable if the value is okay
        // Meaning if value is of the same type/of an initialized variable with same type
        for (String assignment : tokens) {
            String assigned = assignment.split("=")[0];
            String value = assignment.split("=")[1];

            Variable variable = variables.getByName(assigned);

            VariableType assignmentType = getExpressionType(value, variables);
            if (assignmentType == VariableType.IDENTIFIER) {
                if (!variables.getByName(value).isInitialized()) {
                    throw new IllegalLineException("variable is not initialized");
                    // todo: throw exception
                }

                assignmentType = variables.getByName(value).getType();
            }

            variable.initialize(assignmentType);
        }
    }

    public static void parseConditionalStatement(VariablesTable variables,
                                                 String line) throws IllegalLineException {

        String[] conditions = line.split("(\\s+|if|while|\\(|\\)|\\|\\||\\&\\&|\\{)");

        for (String condition : conditions) {
            VariableType variableType = getExpressionType(condition, variables);

            if (variableType == VariableType.IDENTIFIER) {
                if (!VariableType.BOOLEAN.canAccept(variables.getByName(condition).getType())) {
                    // TODO: Throw exception
                    throw new IllegalLineException("variable type can't be converted to boolean");
                }
                if (!variables.getByName(condition).isInitialized()) {
                    throw new IllegalLineException("variable is not initialized");
                    // TODO: throw exception
                }
                continue;
            }

            if (!VariableType.BOOLEAN.canAccept(variableType)) {
                // TODO: Throw exception - wrong type
            }
        }
    }


    public static void parseMethodCall(MethodsTable methods, VariablesTable variables, String line)
            throws IllegalLineException {

        String[] tokens = line.split("(\\s*\\(\\s*|\\s*,\\s*|\\s*\\)\\s*;\\s*)");

        Method method = methods.getByName(tokens[0]);

        if (tokens.length - 1 != method.getParameters().size()) {
            throw new IllegalLineException("not enough parameters");
            // TODO: throw not enough parameters
        }

        for (int i = 1; i < tokens.length; i++) {
            Variable parameter = method.getParameters().get(i - 1).getValue();

            VariableType variableType = getExpressionType(tokens[i], variables);

            if (variableType == VariableType.IDENTIFIER) {
                Variable variable = variables.getByName(tokens[i]);

                if (!variable.isInitialized()) {
                    // TODO: throw uninitialized error
                    throw new IllegalLineException("Uninitialized parameter passed");

                }

                variableType = variable.getType();
            }

            if (!parameter.getType().canAccept(variableType)) {
                // TODO: throw not same type
                throw new IllegalLineException("parameter of not same type");
            }
        }
    }


    private static Pattern intValuePattern = Pattern.compile(RegexConstants.INT_VALUE);
    private static Pattern doubleValuePattern = Pattern.compile(RegexConstants.DOUBLE_VALUE);
    private static Pattern charValuePattern = Pattern.compile(RegexConstants.CHAR_VALUE);
    private static Pattern stringValuePattern = Pattern.compile(RegexConstants.STRING_VALUE);
    private static Pattern booleanValuePattern = Pattern.compile(RegexConstants.BOOLEAN_VALUE);
    private static Pattern identifierValuePattern = Pattern.compile(RegexConstants.IDENTIFIER);

    public static VariableType getExpressionType(String expression, VariablesTable table) {
        if (intValuePattern.matcher(expression).matches()) {
            return VariableType.INT;
        } else if (doubleValuePattern.matcher(expression).matches()) {
            return VariableType.DOUBLE;
        } else if (charValuePattern.matcher(expression).matches()) {
            return VariableType.CHAR;
        } else if (stringValuePattern.matcher(expression).matches()) {
            return VariableType.STRING;
        } else if (booleanValuePattern.matcher(expression).matches()) {
            return VariableType.BOOLEAN;
        } else if (identifierValuePattern.matcher(expression).matches()) {
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
