package opp6.ex6.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static opp6.ex6.main.RegularExpressions.*;

public class VariableFactory {
    private static final ArrayList<String> INT_VALID_TYPES = new ArrayList<>(List.of("int"));
    private static final ArrayList<String> DOUBLE_VALID_TYPES = new ArrayList<>(List.of("double", "int"));
    private static final ArrayList<String> CHAR_VALID_TYPES =  new ArrayList<>(List.of("char"));
    private static final ArrayList<String> STRING_VALID_TYPES =  new ArrayList<>(List.of("String"));
    private static final ArrayList<String> BOOLEAN_VALID_TYPES =  new ArrayList<>(List.of("boolean",
            "double", "int"));

    public static Variable createVariable(String type, boolean global){
        ArrayList<String> validTypes = validTypeAssignment(type);
        assert (validTypes != null);
        Pattern[] regexExpressions = new Pattern[validTypes.size()];
        for (int i = 0; i < validTypes.size(); i ++) {
            regexExpressions[i] = typeRegex(validTypes.get(i));
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

    private static ArrayList<String> validTypeAssignment(String type){
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
