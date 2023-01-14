package opp6.ex6.main;

import java.util.regex.Pattern;

public class RegularExpressions {
    public static final String TYPE = "(int|double|String|char|boolean)";
    public static final String  INT = "int",
                                DOUBLE = "double",
                                STRING = "String",
                                CHAR = "char",
                                BOOLEAN = "boolean";
    public static final String  VOID = "void",
                                FINAL = "final",
                                IF = "if",
                                WHILE = "while",
                                TRUE = "true",
                                FALSE = "false",
                                RETURN = "return";
    public static final String  IS_INT = "([-\\+]?\\d+)",
                                IS_DOUBLE = "([+-]?(?:\\d*\\.\\d+)|(?:\\d+\\.\\d*))",
                                IS_CHAR = "('.')",
                                IS_STRING = "(\"[^,'\"\\\\]*\")",
                                IS_BOOLEAN = "(" + TRUE + "|" + FALSE + ")";

    public static final String EQUAL = "=",
                                MANDATORY_SPACE = "\\s",
                                POSSIBLE_SPACE = "\\s*",
                                COLON =";",
                                COMA = ",";
    public static final String ASSIGNMENT_VALUE = "("+IS_CHAR + "|" + IS_STRING + "|" + "(?:[^\\s;,]+))";


    public static final String VAR_NAME_REGEX = "((?:[a-zA-Z][_\\w]*)|(?:_[_\\w]+))";
    public static final String ALL_BOOLEAN_REGEX =
           "(?:" + IS_BOOLEAN + "|" + IS_DOUBLE + "|" + IS_INT + ")";
    public static final String BOOLEAN_VAR_REGEX = "(?:"+ALL_BOOLEAN_REGEX + "|" + VAR_NAME_REGEX + ")";
    public static final String AND_OR = "(?:(?:\\|\\|)|(?:&&))";
    public static final String CONDITION_REGEX =
            "("+POSSIBLE_SPACE+ALL_BOOLEAN_REGEX+
                    "(?:"+POSSIBLE_SPACE+AND_OR+POSSIBLE_SPACE+BOOLEAN_VAR_REGEX+")*"+POSSIBLE_SPACE+")";
    public static final String WHILE_IF_REGEX =
            POSSIBLE_SPACE+"(?:"+IF+"|"+WHILE+")"+POSSIBLE_SPACE+"\\("+CONDITION_REGEX + "\\)" +
                    POSSIBLE_SPACE + COLON + POSSIBLE_SPACE;


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
    public static Pattern TYPE_PATTERN = Pattern.compile(POSSIBLE_SPACE + TYPE + MANDATORY_SPACE);
    public static Pattern VAR_NAME_PATTERN = Pattern.compile(POSSIBLE_SPACE + VAR_NAME_REGEX);
    public static Pattern ASSIGN_PATTERN = Pattern.compile(
            POSSIBLE_SPACE + EQUAL + POSSIBLE_SPACE +  ASSIGNMENT_VALUE);
    public static Pattern COLON_PATTERN = Pattern.compile(
            POSSIBLE_SPACE + COLON + POSSIBLE_SPACE + "$");
    public static Pattern COMA_PATTERN = Pattern.compile(POSSIBLE_SPACE + COMA);
    public static Pattern INT_PATTERN = Pattern.compile(IS_INT);
    public static Pattern DOUBLE_PATTERN = Pattern.compile(IS_DOUBLE);
    public static Pattern BOOLEAN_PATTERN = Pattern.compile(IS_BOOLEAN);
    public static Pattern CHAR_PATTERN = Pattern.compile(IS_CHAR);
    public static Pattern STRING_PATTERN = Pattern.compile(IS_STRING);
    public static Pattern WHILE_IF_PATTERN = Pattern.compile(WHILE_IF_REGEX);
    public static Pattern ALL_BOOLEAN_PATTERN = Pattern.compile(ALL_BOOLEAN_REGEX);
}
