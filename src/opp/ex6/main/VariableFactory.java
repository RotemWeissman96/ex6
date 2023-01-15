package opp.ex6.main;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static opp.ex6.main.RegularExpressions.*;

public class VariableFactory {
    //these are array list of all the types weh have
    private static final ArrayList<String> INT_VALID_TYPES = new ArrayList<>(List.of(INT));
    private static final ArrayList<String> DOUBLE_VALID_TYPES = new ArrayList<>(List.of(DOUBLE, INT));
    private static final ArrayList<String> CHAR_VALID_TYPES =  new ArrayList<>(List.of(CHAR));
    private static final ArrayList<String> STRING_VALID_TYPES =  new ArrayList<>(List.of(STRING));
    public static final ArrayList<String> BOOLEAN_VALID_TYPES =  new ArrayList<>(List.of(BOOLEAN,
            DOUBLE, INT));


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
        switch (type){
            case INT:
                return RegularExpressions.INT_PATTERN;
            case DOUBLE:
                return RegularExpressions.DOUBLE_PATTERN;
            case CHAR:
                return RegularExpressions.CHAR_PATTERN;
            case STRING:
                return RegularExpressions.STRING_PATTERN;
            case BOOLEAN:
                return RegularExpressions.BOOLEAN_PATTERN;
        }
        return null;
    }

    /**
     *  a list of all the types that can work for this type
     * @param type the type of the Variable
     * @return a list of strings that fit this type
     */
    private static ArrayList<String> validTypeAssignment(String type){
        switch (type){
            case INT:
                return INT_VALID_TYPES;
            case DOUBLE:
                return DOUBLE_VALID_TYPES;
            case CHAR:
                return CHAR_VALID_TYPES;
            case STRING:
                return STRING_VALID_TYPES;
            case BOOLEAN:
                return BOOLEAN_VALID_TYPES;
        }
        return null;
    }
}
