package opp6.ex6.main;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Variable {
    private final String type;
    private final boolean global;
    private boolean finalVariable;
    private boolean value;
    private Pattern[] typesPatterns; // its a list so we can support double to be assigned with int and double
    private ArrayList<String> allowedTypesAssignment;

    public Variable(String type, boolean global,  Pattern[] typesPatterns,
                    ArrayList<String> allowedTypesAssignment) {
        this.type = type;
        this.global = global;
        this.finalVariable = false;
        this.typesPatterns = typesPatterns;
        this.allowedTypesAssignment = allowedTypesAssignment;
        this.value = false;
    }

    public String getType() {
        return type;
    }

    public boolean getGlobal(){
        return global;
    }

    public boolean getValue(){
        return value;
    }

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

    public void setFinale(boolean finalVariable) {
        if (this.finalVariable){
            return;
        }
        this.finalVariable = finalVariable;
    }
}
