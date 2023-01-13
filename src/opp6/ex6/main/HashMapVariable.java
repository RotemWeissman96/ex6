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


    /**
     * get a line that was already compiled and the next 1 or 2 words should be a variable and an
     * assignment or a variable without an assignment
     * @param line the current line to compile
     * @param type the type of variable
     * @param global is this variable global
     * @param finalVariable is this variable final
     * @return the line after the variable declaration
     */
    public String addVariableFromLineStart(String line, String type, boolean global, boolean finalVariable){
        // throws InvalidValue / WrongSyntax
        // check type
        String name = "";
        Matcher matcher = VAR_NAME_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            name = matcher.group(1);
            if (this.currentScope.containsKey(name)){
                System.out.println("raise error 8");
            }
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error 2");
        }
        Variable variable = VariableFactory.createVariable(type, global);
        matcher = ASSIGN_PATTERN.matcher(line);
        this.currentScope.put(name, variable);
        if (matcher.lookingAt()) {
            variable.setValue(matcher.group(1), this);
            variable.setFinale(finalVariable); // set final to true only after value assignment
            line = line.substring(matcher.end());
        } else {
            if (finalVariable) { // if it's a final variable there must be assignment
                System.out.println("raise error 1");
            }
        }
        return line;
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
        System.out.println("currentScope");
        this.currentScope.forEach((key, value) -> System.out.println(key + ":" + value.getType()));
        System.out.println("outerScope");
        this.outerScope.forEach((key, value) -> System.out.println(key + ":" + value.getType()));
    }
}
