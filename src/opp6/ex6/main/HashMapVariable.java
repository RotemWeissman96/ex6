package opp6.ex6.main;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static opp6.ex6.main.RegularExpressions.*;

public class HashMapVariable {

    private static final String[] ALLOWED_TYPES = {"String", "int", "char", "double", "boolean"};
    private static final String[] VARIABLE_DECORATORS = {"final"};

    private final HashMap<String, Variable> currentScope;
    private final HashMap<String, Variable> outerScope;
    public HashMapVariable() {
        currentScope = new HashMap<>();
        outerScope = new HashMap<>();
    }

    public HashMapVariable(HashMapVariable other) { // copy constructor
        currentScope = new HashMap<>();
        outerScope = new HashMap<>();
        for (String variable : other.outerScope.keySet()){ // deep copy outer scope to outer scope
            this.outerScope.put(variable, other.outerScope.get(variable));
        }
        for (String variable : other.currentScope.keySet()){ // deep copy other.inner scope to outer scope
            this.outerScope.put(variable, other.currentScope.get(variable));
        }
    }

    public void createVariable(String line, boolean global) { // throws InvalidValue / WrongSyntax
        String type= null;
        boolean finalVariable = false;
        // check type
        Matcher matcher = FINAL_PATTERN.matcher(line);
        if (matcher.lookingAt()){
            finalVariable = true;
            line = line.substring(matcher.end());
        }
        // check type
        matcher = TYPE_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            type = matcher.group(1);
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error");
        }
        //check type
        matcher = VAR_NAME_PATTERN.matcher(line);
//        if(line.startsWith(INT)){
//            matcher = LEGAL_INT_VARIABLE.matcher(line);
//            type = INT;
//        } else if (line.startsWith(DOUBLE)) {
//            matcher = LEGAL_DOUBLE_VARIABLE.matcher(line);
//            type = DOUBLE;
//        } else if (line.startsWith(STRING)) {
//            matcher = LEGAL_STRING_VARIABLE.matcher(line);
//            type = STRING;
//        } else if (line.startsWith(CHAR)) {
//            matcher = LEGAL_CHAR_VARIABLE.matcher(line);
//            type = CHAR;
//        } else if (line.startsWith(BOOLEAN)) {
//            matcher = LEGAL_BOOLEAN_VARIABLE.matcher(line);
//            type = BOOLEAN;
//        }

        assert (matcher != null);
        if (matcher.matches()){
            Variable variable = VariableFactory.createVariable(type, global, finalVariable);
        }
        //TODO: analyze line (decide if its legal) and create a new variable from it
        //TODO: need to handle multi declarations in 1 line
        //TODO: use the factory and add to the current map
    }

    public static boolean isLineVariableDeclaration(String line){
        for (String type : ALLOWED_TYPES){
            if (line.startsWith(type)){
                return true;
            }
        }
        for (String dec : VARIABLE_DECORATORS){
            if (line.startsWith(dec)){
                return true;
            }
        }
        return false;
    }

    public Variable getCurrentScope(String name){ // throw VariableDoNotExist
        if (currentScope.containsKey(name)){
            return currentScope.get(name);
        }
        return null; // throw VariableDoNotExist
    }

    public Variable getOuterScope(String name) {
        if (outerScope.containsKey(name)){
            return outerScope.get(name);
        }
        return null; // throw VariableDoNotExist
    }
}
