package oop.ex6.main;

import java.util.regex.Pattern;

public class RegularExpressions {
    // basic key wards and types
    public static final String TYPE_REGEX = "(int|double|String|char|boolean)";
    public static final String INT = "int",
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
                                IS_DOUBLE = "([+-]?(?:(?:\\d*\\.\\d+)|(?:\\d+\\.\\d*)))",
                                IS_CHAR = "('.')",
                                IS_STRING = "(\"[^,'\"\\\\]*\")",
                                IS_BOOLEAN = "(" + TRUE + "|" + FALSE + ")";

    public static final String EQUAL = "=",
                                EMPTY = "",
                                MANDATORY_SPACE = "\\s",
                                POSSIBLE_SPACE = "\\s*",
                                COLON =";",
                                COMA = ",",
                                DOUBLE_LINES = "//",
                                OPEN_BRACKETS = "\\(",
                                CLOSE_BRACKETS = "\\)",
                                CLOSE_CURLY_BRACKETS = "}",
                                OPEN_CURLY_BRACKETS_REGEX = "\\{";

// basic expression (building blocks)
    public static final String ASSIGNMENT_VALUE =
            "("+IS_CHAR + "|" + IS_STRING + "|" + "(?:[\\w_\\.\\+-]+))";
    public static final String VAR_NAME_REGEX = "((?:[a-zA-Z][_\\w]*)|(?:_[_\\w]+))";
    public static final String ALL_BOOLEAN_REGEX =
            "(?:" + IS_BOOLEAN + "|" + IS_DOUBLE + "|" + IS_INT + ")";
    public static final String BOOLEAN_VAR_REGEX = "(?:"+ALL_BOOLEAN_REGEX + "|" + VAR_NAME_REGEX + ")";
    public static final String SPACED_COMA_REGEX = POSSIBLE_SPACE + COMA + POSSIBLE_SPACE;


// method related regex
    public static final String FUNCTION_NAME_REGEX = "([a-zA-Z][_\\w]*)";
    public static final String ENDING_HEAD_FUN_REGEX =
            POSSIBLE_SPACE + CLOSE_BRACKETS + POSSIBLE_SPACE + OPEN_CURLY_BRACKETS_REGEX + POSSIBLE_SPACE;
    public static final String ENDING_METHOD_CALL =
            POSSIBLE_SPACE + CLOSE_BRACKETS + POSSIBLE_SPACE + COLON + POSSIBLE_SPACE;
    public static final String RETURN_LINE_REGEX =
            POSSIBLE_SPACE + RETURN + POSSIBLE_SPACE + COLON + POSSIBLE_SPACE;
    public static final String METHOD_ARGUMENTS_REGEX =
            "((?:" + POSSIBLE_SPACE + ASSIGNMENT_VALUE + POSSIBLE_SPACE + ")?(?:"  + "," + POSSIBLE_SPACE +
                    ASSIGNMENT_VALUE + POSSIBLE_SPACE + ")*)";


//if/while related regex
    public static final String AND_OR = "(?:(?:\\|\\|)|(?:&&))";
    public static final String CONDITION_REGEX =
            "("+POSSIBLE_SPACE+BOOLEAN_VAR_REGEX+
                    "(?:"+POSSIBLE_SPACE+AND_OR+POSSIBLE_SPACE+BOOLEAN_VAR_REGEX+")*"+POSSIBLE_SPACE+")";
    public static final String WHILE_IF_REGEX =
            POSSIBLE_SPACE+"(?:"+IF+"|"+WHILE+")"+POSSIBLE_SPACE+"\\("+ CONDITION_REGEX + "\\)" +
                    POSSIBLE_SPACE + OPEN_CURLY_BRACKETS_REGEX + POSSIBLE_SPACE;


// Patterns

    //basic
    public static Pattern FINAL_PATTERN = Pattern.compile(POSSIBLE_SPACE + FINAL + MANDATORY_SPACE);
    public static Pattern TYPE_PATTERN = Pattern.compile(POSSIBLE_SPACE + TYPE_REGEX + MANDATORY_SPACE);
    public static Pattern VAR_NAME_PATTERN = Pattern.compile(POSSIBLE_SPACE + VAR_NAME_REGEX);
    public static Pattern COLON_PATTERN = Pattern.compile(
            POSSIBLE_SPACE + COLON + POSSIBLE_SPACE + "$");
    public static Pattern COMA_PATTERN = Pattern.compile(POSSIBLE_SPACE + COMA);

// method related
    public static Pattern VOID_PATTERN =
            Pattern.compile(POSSIBLE_SPACE + VOID + MANDATORY_SPACE);
    public static Pattern FUNCTION_NAME_PATTERN =
            Pattern.compile(POSSIBLE_SPACE + FUNCTION_NAME_REGEX + POSSIBLE_SPACE + OPEN_BRACKETS);
    public static Pattern NEXT_ARGUMENT_PATTERN = Pattern.compile(SPACED_COMA_REGEX);
    public static Pattern ENDING_HEAD_FUN_PATTERN = Pattern.compile(ENDING_HEAD_FUN_REGEX);
    public static Pattern RETURN_LINE_PATTERN = Pattern.compile(RETURN_LINE_REGEX);
    public static Pattern END_METHOD_CALL_PATTERN = Pattern.compile(ENDING_METHOD_CALL);
    public static Pattern IS_METHOD_CALL_PATTERN = Pattern.compile(
            POSSIBLE_SPACE + FUNCTION_NAME_REGEX + POSSIBLE_SPACE + "\\(");

//variable related
    public static Pattern ASSIGN_PATTERN = Pattern.compile(
            POSSIBLE_SPACE + EQUAL + POSSIBLE_SPACE +  ASSIGNMENT_VALUE);
    public static Pattern INT_PATTERN = Pattern.compile(IS_INT);
    public static Pattern DOUBLE_PATTERN = Pattern.compile(IS_DOUBLE);
    public static Pattern BOOLEAN_PATTERN = Pattern.compile(IS_BOOLEAN);
    public static Pattern CHAR_PATTERN = Pattern.compile(IS_CHAR);
    public static Pattern STRING_PATTERN = Pattern.compile(IS_STRING);
    public static Pattern ARGUMENT_PATTERN = Pattern.compile(POSSIBLE_SPACE + ASSIGNMENT_VALUE);
    public static Pattern VALID_ASSIGNMENT_PATTERN = Pattern.compile(POSSIBLE_SPACE + VAR_NAME_REGEX +
            ASSIGN_PATTERN.pattern() + "(?:" + SPACED_COMA_REGEX + VAR_NAME_REGEX +  ASSIGN_PATTERN.pattern()
            + ")*" + POSSIBLE_SPACE + COLON + POSSIBLE_SPACE);

// if /while related
    public static Pattern WHILE_IF_PATTERN = Pattern.compile(WHILE_IF_REGEX);
    public static Pattern ALL_BOOLEAN_PATTERN = Pattern.compile(ALL_BOOLEAN_REGEX);
    public static Pattern AND_OR_PATTERN = Pattern.compile(POSSIBLE_SPACE + AND_OR + POSSIBLE_SPACE);
}
