package opp6.ex6.main;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

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

    public void printMaps(){
        System.out.println("\n====printing maps===\n");
        System.out.println("==currentScope==\n");
        this.currentScope.forEach((key, value) -> System.out.println(key + ":" + value.getType()));
        System.out.println("\n==outerScope==\n");
        this.outerScope.forEach((key, value) -> System.out.println(key + ":" + value.getType()));
    }
    public void putCurrentScope(String name, Variable variable){
        this.currentScope.put(name,variable);
    }
}
