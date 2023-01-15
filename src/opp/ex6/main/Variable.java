package opp.ex6.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {
    private final String type;
    private final boolean global;
    private boolean finalVariable;
    private boolean value;
    private Pattern[] typesPatterns; // it's a list, so we can support double to be assigned with int and double
    private ArrayList<String> allowedTypesAssignment;

    /**
     *
     * @param type
     * @param global
     * @param typesPatterns
     * @param allowedTypesAssignment
     */
    public Variable(String type, boolean global,  Pattern[] typesPatterns,
                    ArrayList<String> allowedTypesAssignment) {
        this.type = type;
        this.global = global;
        this.finalVariable = false;
        this.typesPatterns = typesPatterns;
        this.allowedTypesAssignment = allowedTypesAssignment;
        this.value = false;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return
     */
    public boolean getGlobal(){
        return global;
    }

    /**
     *
     * @return
     */
    public boolean getValue(){
        return value;
    }

    /**
     *
     * @param value
     * @param map
     */
    public void setValue(String value, HashMapVariable map) {
        // throws invalidValueException or VariableDoesNotExist (for variable assignment)
        //TODO: analyze the value string, throw exception if its not legal
        if (this.finalVariable){ // cant assign values to final variables
            System.out.println("raise error: cant assign a value to finale variable");
        }
        for (Pattern p : this.typesPatterns) { //if it's a valid value to assign
            if (p.matcher(value).matches()){
                this.value = true;
                return;
            }
        }
        if (map.getCurrentScope(value) != null &&
                this.allowedTypesAssignment.contains(map.getCurrentScope(value).getType())) {
            // if there is a current scope variable - its deciding

            if (map.getCurrentScope(value).getValue()){
                this.value = true;
            } else {
                System.out.println("raise error: there is an inner scope variable with different type " +
                        "or a null");
                System.out.println(this.type + " : " + value);
            }
        } else { // if there is only outer scope variable - only then its deciding
            if (map.getOuterScope(value) != null  &&
                    this.allowedTypesAssignment.contains(map.getOuterScope(value).getType())) {
                if (map.getOuterScope(value).getValue()){
                    this.value = true;
                } else {
                    System.out.println("raise error: there is an outer scope variable with different type " +
                            "or a null");
                    System.out.println(this.type + " : " + value);
                }
            } else {
                System.out.println("raise error: not a valid assignment");
                System.out.println(this.type + " : " + value);
            }
        }
    }

    /**
     *
     */
    public void setValueTrue(){
        this.value = true;
    }

    /**
     *
     * @param line
     * @param global
     * @param map
     */
    public static void compileVariableDeclaration(String line, boolean global, HashMapVariable map) {
        String type = null;
        boolean finalVariable = false;
        // check final
        Matcher matcher = RegularExpressions.FINAL_PATTERN.matcher(line);
        if (matcher.lookingAt()){
            finalVariable = true;
            line = line.substring(matcher.end());
        }
        // check type
        matcher = RegularExpressions.TYPE_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            type = matcher.group(1);
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error: that variable type does not exist");
        }
        // check for variables
        line = addVariableFromLineStart(line, type, global, finalVariable, map);
        matcher = RegularExpressions.COMA_PATTERN.matcher(line);
        // if there is "," then there is another variable
        while (matcher.lookingAt()) {
            line = line.substring(matcher.end());
            line = addVariableFromLineStart(line, type, global, finalVariable, map);
            matcher = RegularExpressions.COMA_PATTERN.matcher(line);
        }
        matcher = RegularExpressions.COLON_PATTERN.matcher(line);
        if (!matcher.lookingAt()) {  // end of line must be ;
            System.out.println("raise error: line must end with ;");
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
    public static String addVariableFromLineStart(String line, String type, boolean global, boolean finalVariable,
                                                  HashMapVariable map){
        // throws InvalidValue / WrongSyntax
        // check type
        Variable variable = VariableFactory.createVariable(type, global);
        line = map.validatingName(line, variable, false);
        Matcher matcher = RegularExpressions.ASSIGN_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            variable.setValue(matcher.group(1), map);
            variable.setFinale(finalVariable); // set final to true only after value assignment
            line = line.substring(matcher.end());
        } else {
            if (finalVariable) { // if it's a final variable there must be assignment
                System.out.println("raise error: cant declare a final with no assignment");
            }
        }
        return line;
    }

    /**
     *
     * @param finalVariable
     */
    public void setFinale(boolean finalVariable) {
        if (this.finalVariable){
            return;
        }
        this.finalVariable = finalVariable;
    }
}
