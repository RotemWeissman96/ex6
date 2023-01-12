package opp6.ex6.main;

import java.util.regex.Pattern;

public class Variable {
    private final String type;
    private final boolean global;
    private final boolean finalVariable;
    private boolean value;
    private String[] valueRegex; // its a list so we can support double to be assigned with int
    private String[] allowedTypesAssignment;

    public Variable(String type, boolean global, boolean finalVariable,  String[] valueRegex,
                    String[] allowedTypesAssignment) {
        this.type = type;
        this.global = global;
        this.finalVariable = finalVariable;
        this.valueRegex = valueRegex;
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
    }
}
