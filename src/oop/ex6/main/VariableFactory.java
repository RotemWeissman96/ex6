package oop.ex6.main;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class VariableFactory {
    //these are array list of all the types weh have
    private static final ArrayList<String> INT_VALID_TYPES = new ArrayList<>(List.of(RegularExpressions.INT));
    private static final ArrayList<String> DOUBLE_VALID_TYPES = new ArrayList<>(List.of(RegularExpressions.DOUBLE, RegularExpressions.INT));
    private static final ArrayList<String> CHAR_VALID_TYPES =  new ArrayList<>(List.of(RegularExpressions.CHAR));
    private static final ArrayList<String> STRING_VALID_TYPES =  new ArrayList<>(List.of(RegularExpressions.STRING));
    public static final ArrayList<String> BOOLEAN_VALID_TYPES =  new ArrayList<>(List.of(RegularExpressions.BOOLEAN,
            RegularExpressions.DOUBLE, RegularExpressions.INT));


    /**
     * creates a variable
     * @param type the type of the variable
     * @return the Variable
     */
    public static Variable createVariable(String type){
        ArrayList<String> validTypes = validTypeAssignment(type);
        assert (validTypes != null);
        Pattern[] regexExpressions = new Pattern[validTypes.size()];
        for (int i = 0; i < validTypes.size(); i ++) {
            regexExpressions[i] = typeRegex(validTypes.get(i));
        }
        return new Variable(type, regexExpressions, validTypes);
    }


    /**
     * getting the pattern for each type
     * @param type the type of the Variable
     * @return a pattern suited for type
     */
    private static Pattern typeRegex(String type){
        switch (type) {
            case RegularExpressions.INT:
                return RegularExpressions.INT_PATTERN;
            case RegularExpressions.DOUBLE:
                return RegularExpressions.DOUBLE_PATTERN;
            case RegularExpressions.CHAR:
                return RegularExpressions.CHAR_PATTERN;
            case RegularExpressions.STRING:
                return RegularExpressions.STRING_PATTERN;
            case RegularExpressions.BOOLEAN:
                return RegularExpressions.BOOLEAN_PATTERN;
            default:
                return null;
        }
    }

    /**
     *  a list of all the types that can work for this type
     * @param type the type of the Variable
     * @return a list of strings that fit this type
     */
    private static ArrayList<String> validTypeAssignment(String type){
        switch (type) {
            case RegularExpressions.INT:
                return INT_VALID_TYPES;
            case RegularExpressions.DOUBLE:
                return DOUBLE_VALID_TYPES;
            case RegularExpressions.CHAR:
                return CHAR_VALID_TYPES;
            case RegularExpressions.STRING:
                return STRING_VALID_TYPES;
            case RegularExpressions.BOOLEAN:
                return BOOLEAN_VALID_TYPES;
            default: return null;
        }
    }
}
