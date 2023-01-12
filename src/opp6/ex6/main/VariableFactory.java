package opp6.ex6.main;

public class VariableFactory {
    private static final String[] INT_VALID_TYPES = {"double", "int"};
    private static final String[] DOUBLE_VALID_TYPES = {"int"};
    private static final String[] CHAR_VALID_TYPES = {"char"};
    private static final String[] STRING_VALID_TYPES = {"String"};
    private static final String[] BOOLEAN_VALID_TYPES = {"boolean", "double", "int"};
    private static final String INT_REGEX = "";
    private static final String DOUBLE_REGEX = "";
    private static final String CHAR_REGEX = "";
    private static final String STRING_REGEX = "";
    private static final String BOOLEAN_REGEX = "";



    public static Variable createVariable(String type, boolean global, boolean finalVariable){
        String[] validTypes = validTypeAssignment(type);
        assert (validTypes != null);
        String[] regexExpressions = new String[validTypes.length];
        for (int i = 0; i < validTypes.length; i ++) {
            regexExpressions[i] = typeRegex(validTypes[i]);
        }
        return new Variable(type, global, finalVariable, regexExpressions, validTypes);
    }

    private static String typeRegex(String type){
        switch (type){
            case "int":
                return INT_REGEX;
            case "double":
                return DOUBLE_REGEX;
            case "char":
                return CHAR_REGEX;
            case "String":
                return STRING_REGEX;
            case "boolean":
                return BOOLEAN_REGEX;
        }
        return "";
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
