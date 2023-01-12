package opp6.ex6.main;

import java.util.HashMap;

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
