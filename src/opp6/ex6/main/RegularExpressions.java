package opp6.ex6.main;

import java.util.regex.Pattern;

public class RegularExpressions {
    public static final String TYPE = "(int|double|String|char|boolean)";
    public static final String INT = "int",
            DOUBLE = "double",
            STRING = "String",
            CHAR = "char",
            BOOLEAN = "boolean";
    public static final
            String VOID = "void",
            FINAL = "final",
            IF = "if",
            WHILE = "while",
            TRUE = "true",
            FALSE = "false",
            RETURN = "return";
    public static final String IS_INT = "([-\\+]?*\\d+)",
            IS_DOUBLE = "(\\d*.\\d+) | (\\d+.\\d*)",
            IS_CHAR = ".",
            IS_STRING = "\".*\"",
            IS_BOOLEAN = TRUE + "|" + FALSE;

    public static final String EQUAL = "=", MANDATORY_SPACE = "\\s", POSSIBLE_SPACE = "\\s*", COLON =";";
    public static final String ASSIGNMENT_VALUE = "(\\S+)";
    public static final String VAR_NAME_REGEX = "((?:[a-zA-Z][_\\w]*)|(?:_[_\\w]+))";



//    public static final String SINGLE_ASSIGNMENT =
//            VAR_NAME_REGEX + POSSIBLE_SPACE + EQUAL + POSSIBLE_SPACE + ASSIGNMENT_VALUE;
//    public static final String SINGLE_VARIABLE_DECLARATION_ASSIGN =
//            TYPE + MANDATORY_SPACE + SINGLE_ASSIGNMENT + POSSIBLE_SPACE + COLON + POSSIBLE_SPACE;
//    public static final String SINGLE_VARIABLE_DECLARATION =
//            TYPE + MANDATORY_SPACE + VAR_NAME_REGEX + POSSIBLE_SPACE + COLON + POSSIBLE_SPACE;
//    public static final String MULTI_VARIABLE_DECLARATION_ASSIGN =
//            TYPE + MANDATORY_SPACE + SINGLE_ASSIGNMENT + POSSIBLE_SPACE + COLON + POSSIBLE_SPACE;
//    public static final String MULTI_VARIABLE_DECLARATION =
//            TYPE + MANDATORY_SPACE + VAR_NAME_REGEX + POSSIBLE_SPACE + COLON + POSSIBLE_SPACE;
//
//
//    public static Pattern LEGAL_VARIABLE = Pattern.compile(VARIABLE_ASSIGN);
    public static Pattern FINAL_PATTERN = Pattern.compile(POSSIBLE_SPACE + FINAL + MANDATORY_SPACE);

}
