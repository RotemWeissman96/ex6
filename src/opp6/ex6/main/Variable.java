package opp6.ex6.main;

import java.util.regex.Pattern;

public class Variable {
    private final String type;
    private final boolean global;
    private boolean finalVariable;
    private boolean value;
    private Pattern[] typesPatterns; // its a list so we can support double to be assigned with int and double
    private String[] allowedTypesAssignment;

    public Variable(String type, boolean global,  Pattern[] typesPatterns,
                    String[] allowedTypesAssignment) {
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
            System.out.println("raise error 8");
        }
        for (Pattern p : this.typesPatterns) { //if it's a valid value to assign
            if (p.matcher(value).matches()){
                this.value = true;
                return;
            }
        }
        if (map.getCurrentScope(value) != null) { // if there is a current scope variable - its deciding
            if (map.getCurrentScope(value).getValue()){ // TODO: check its its correct type
                this.value = true;
            } else {
                System.out.println("raise error 7");
            }
        } else { // if there is only outer scope variable - only then its deciding
            if (map.getOuterScope(value) != null) { // TODO: check its its correct type
                if (map.getOuterScope(value).getValue()){
                    this.value = true;
                } else {
                    System.out.println("raise error 6");
                }
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
