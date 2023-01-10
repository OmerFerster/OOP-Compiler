package oop.ex6.checker;

import oop.ex6.utils.RegexConstants;

import java.util.regex.Pattern;


public enum LineType {

    EMPTY_LINE(RegexConstants.EMPTY_LINE),
    COMMENT_LINE(RegexConstants.COMMENT_LINE),

    INT_VAR_DECLARATION(RegexConstants.INT_VARIABLE),
    DOUBLE_VAR_DECLARATION(RegexConstants.DOUBLE_VARIABLE),
    CHAR_VAR_DECLARATION(RegexConstants.CHAR_VARIABLE),
    BOOLEAN_VAR_DECLARATION(RegexConstants.BOOLEAN_VARIABLE),
    STRING_VAR_DECLARATION(RegexConstants.STRING_VARIABLE),

    IDENTIFIER_VAR_ASSIGNMENT(RegexConstants.IDENTIFIER_ASSIGMENT),
    INT_VAR_ASSIGMENT(RegexConstants.INT_ASSIGMENT),
    DOUBLE_VAR_ASSIGMENT(RegexConstants.DOUBLE_ASSIGMENT),
    CHAR_VAR_ASSIGMENT(RegexConstants.CHAR_ASSIGMENT),
    BOOLEAN_VAR_ASSIGMENT(RegexConstants.BOOLEAN_ASSIGMENT),
    STRING_VAR_ASSIGMENT(RegexConstants.STRING_ASSIGMENT),

    IF(RegexConstants.IF_STATEMENT),
    WHILE(RegexConstants.WHILE_STATEMENT),

    METHOD_DECLARATION(RegexConstants.METHOD_DECLARATION),
    METHOD_CALL(RegexConstants.METHOD_CALL),

    RETURN(RegexConstants.RETURN),
    BLOCK_CLOSE(RegexConstants.BLOCK_CLOSER);


    private final Pattern pattern;

    LineType(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return this.pattern;
    }
}
