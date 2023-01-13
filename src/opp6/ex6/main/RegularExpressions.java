package opp6.ex6.main;

import java.util.regex.Pattern;

public class RegularExpressions {
    public static final String TYPE = "(int|double|String|char|boolean)";
    public static final String INT = ("int"),
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
    public static final String IS_INT = "(\\s*[-\\+]?*\\d+)",
            IS_DOUBLE = "(" +"(" + IS_INT + ")" + "|"+"(\\d*.\\d+)"+ ")",
            IS_CHAR = "'.'\\s*",
            IS_STRING = "\".*\"\\s*",
            IS_BOOLEAN = IS_INT + "|" + IS_DOUBLE + "|" + TRUE + "|" + FALSE;

    public static final String EQUAL = "=", WHITE_SPACE_REGEX = "\\s+", COLON ="\\s*;\\s*";
    public static final String ASSIGNMENT = "\\s*(\\S+)\\s*";
    public static final String ASSIGNMENT = "\\s*(\\S+)\\s*";


//    public static final String VAR_NAME_REGEX = "\\s*(([a-zA-Z]+\\w*)|(_\\w+)|(__+\\w*))\\s*_[_\\w]+";
    public static final String VAR_NAME_REGEX = "(\\s*(?:[a-zA-Z][_\\w]*)|(?:_[_\\w]+)\\s*)";

    public static final String VARIABLE_ASSIGN =
            TYPE + WHITE_SPACE_REGEX + VAR_NAME_REGEX + EQUAL + ASSIGNMENT + COLON;


    public static Pattern LEGAL_VARIABLE = Pattern.compile(VARIABLE_ASSIGN);


}
