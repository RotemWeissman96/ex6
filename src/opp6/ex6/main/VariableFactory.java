package opp6.ex6.main;

import java.util.regex.Pattern;

import static opp6.ex6.main.RegularExpressions.*;

public class VariableFactory {
    private static final String[] INT_VALID_TYPES = {"int"};
    private static final String[] DOUBLE_VALID_TYPES = {"double", "int"};
    private static final String[] CHAR_VALID_TYPES = {"char"};
    private static final String[] STRING_VALID_TYPES = {"String"};
    private static final String[] BOOLEAN_VALID_TYPES = {"boolean", "double", "int"};
    private static final String INT_REGEX = "";
    private static final String DOUBLE_REGEX = "";
    private static final String CHAR_REGEX = "";
    private static final String STRING_REGEX = "";
    private static final String BOOLEAN_REGEX = "";

    public static Variable createVariable(String type, boolean global){
        String[] validTypes = validTypeAssignment(type);
        assert (validTypes != null);
        Pattern[] regexExpressions = new Pattern[validTypes.length];
        for (int i = 0; i < validTypes.length; i ++) {
            regexExpressions[i] = typeRegex(validTypes[i]);
        }
        return new Variable(type, global, regexExpressions, validTypes);
    }

    private static Pattern typeRegex(String type){
        switch (type){
            case "int":
                return INT_PATTERN;
            case "double":
                return DOUBLE_PATTERN;
            case "char":
                return CHAR_PATTERN;
            case "String":
                return STRING_PATTERN;
            case "boolean":
                return BOOLEAN_PATTERN;
        }
        return null;
    }

    private static String[] validTypeAssignment(String type){
        switch (type){
            case "int":
                return INT_VALID_TYPES;
            case "double":
                return DOUBLE_VALID_TYPES;
            case "char":
                return CHAR_VALID_TYPES;
            case "String":
                return STRING_VALID_TYPES;
            case "boolean":
            return BOOLEAN_VALID_TYPES;
        }
        return null;
    }
}
