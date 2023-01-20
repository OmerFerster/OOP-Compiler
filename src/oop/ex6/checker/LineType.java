package oop.ex6.checker;

import oop.ex6.utils.RegexConstants;

import java.util.regex.Pattern;

/**
 * An enum used to distinguish between different lines
 */
public enum LineType {

    EMPTY_LINE(RegexConstants.EMPTY_LINE),
    COMMENT_LINE(RegexConstants.COMMENT_LINE),

    INT_VAR_DECLARATION(RegexConstants.INT_VARIABLE),
    DOUBLE_VAR_DECLARATION(RegexConstants.DOUBLE_VARIABLE),
    CHAR_VAR_DECLARATION(RegexConstants.CHAR_VARIABLE),
    BOOLEAN_VAR_DECLARATION(RegexConstants.BOOLEAN_VARIABLE),
    STRING_VAR_DECLARATION(RegexConstants.STRING_VARIABLE),

    ASSIGNMENT(RegexConstants.ASSIGNMENT),

    IF(RegexConstants.IF_STATEMENT),
    WHILE(RegexConstants.WHILE_STATEMENT),

    METHOD_DECLARATION(RegexConstants.METHOD_DECLARATION),
    METHOD_CALL(RegexConstants.METHOD_CALL),

    RETURN(RegexConstants.RETURN),
    BLOCK_CLOSE(RegexConstants.BLOCK_CLOSER);


    private final Pattern pattern;

    /**
     * @param regex Regex to use to determine the type of a given line
     */
    LineType(String regex) {
        this.pattern = Pattern.compile(regex);
    }


    /**
     * Returns the pattern that matches the LineType object
     *
     * @return LineType's regex pattern
     */
    public Pattern getPattern() {
        return this.pattern;
    }
}
