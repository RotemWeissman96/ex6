package opp.ex6.main;

import java.util.regex.Pattern;

public class RegularExpressions {
    public static final String TYPE = "(int|double|String|char|boolean)";
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
                                MANDATORY_SPACE = "\\s",
                                POSSIBLE_SPACE = "\\s*",
                                COLON =";",
                                COMA = ",",
                                OPEN_BRACKETS = "\\(",
                                CLOSE_BRACKETS = "\\)",
                                OPEN_CURLY_BRACKETS = "\\{",
                                CLOSE_CURLY_BRACKETS = "\\}";

    public static final String ASSIGNMENT_VALUE =
            "("+IS_CHAR + "|" + IS_STRING + "|" + "(?:[\\w_\\.\\+-]+))";
    public static final String VAR_NAME_REGEX = "((?:[a-zA-Z][_\\w]*)|(?:_[_\\w]+))";

    public static final String FUNCTION_NAME_REGEX = "([a-zA-Z][_\\w]*)";
    public static final String ALL_BOOLEAN_REGEX =
           "(?:" + IS_BOOLEAN + "|" + IS_DOUBLE + "|" + IS_INT + ")";
    public static final String BOOLEAN_VAR_REGEX = "(?:"+ALL_BOOLEAN_REGEX + "|" + VAR_NAME_REGEX + ")";
    public static final String AND_OR = "(?:(?:\\|\\|)|(?:&&))";

    public static final String NEXT_ARGUMENT_REGEX =
            POSSIBLE_SPACE + COMA + POSSIBLE_SPACE;
    public static final String ENDING_SCOPE_REGEX =
            POSSIBLE_SPACE + CLOSE_BRACKETS + POSSIBLE_SPACE + OPEN_CURLY_BRACKETS + POSSIBLE_SPACE;
    public static final String ENDING_METHOD_CALL =
            POSSIBLE_SPACE + CLOSE_BRACKETS + POSSIBLE_SPACE + COLON + POSSIBLE_SPACE;
    public static final String CONDITION_REGEX =
            "("+POSSIBLE_SPACE+BOOLEAN_VAR_REGEX+
                    "(?:"+POSSIBLE_SPACE+AND_OR+POSSIBLE_SPACE+BOOLEAN_VAR_REGEX+")*"+POSSIBLE_SPACE+")";
    public static final String WHILE_IF_REGEX =
            POSSIBLE_SPACE+"(?:"+IF+"|"+WHILE+")"+POSSIBLE_SPACE+"\\("+ CONDITION_REGEX + "\\)" +
                    POSSIBLE_SPACE + OPEN_CURLY_BRACKETS + POSSIBLE_SPACE;
    public static final String RETURN_LINE_REGEX =
            POSSIBLE_SPACE + RETURN + POSSIBLE_SPACE + COLON + POSSIBLE_SPACE;
    public static final String METHOD_ARGUMENTS_REGEX =
            "((?:" + POSSIBLE_SPACE + ASSIGNMENT_VALUE + POSSIBLE_SPACE + ")?(?:"  + "," + POSSIBLE_SPACE +
                    ASSIGNMENT_VALUE + POSSIBLE_SPACE + ")*)";



    public static Pattern FINAL_PATTERN = Pattern.compile(POSSIBLE_SPACE + FINAL + MANDATORY_SPACE);
    public static Pattern VOID_PATTERN =
            Pattern.compile(POSSIBLE_SPACE + VOID + MANDATORY_SPACE);
    public static Pattern TYPE_PATTERN = Pattern.compile(POSSIBLE_SPACE + TYPE + MANDATORY_SPACE);

    public static Pattern FUNCTION_NAME_PATTERN =
            Pattern.compile(POSSIBLE_SPACE + FUNCTION_NAME_REGEX + POSSIBLE_SPACE + OPEN_BRACKETS);
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
    public static Pattern NEXT_ARGUMENT_PATTERN = Pattern.compile(NEXT_ARGUMENT_REGEX);
    public static Pattern ENDING_SCOPE_PATTERN = Pattern.compile(ENDING_SCOPE_REGEX);
    public static Pattern RETURN_LINE_PATTERN = Pattern.compile(RETURN_LINE_REGEX);
    public static Pattern ARGUMENT_PATTERN = Pattern.compile(POSSIBLE_SPACE + ASSIGNMENT_VALUE);
    public static Pattern END_METHOD_CALL_PATTERN = Pattern.compile(ENDING_METHOD_CALL);
    public static Pattern IS_METHOD_CALL_PATTERN = Pattern.compile(
            POSSIBLE_SPACE + FUNCTION_NAME_REGEX + POSSIBLE_SPACE + "\\(");
}
