package oop.ex6.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {
    private final String type;
    private boolean finalVariable;
    private boolean value;
    // it's a list, so we can support double to be assigned with int and double
    private final Pattern[] typesPatterns;
    private final ArrayList<String> allowedTypesAssignment;

    /**
     * this is the constructor for the variable
     * @param type the type of the argument

     * @param typesPatterns the list of all the type arguments pattern
     * @param allowedTypesAssignment  the list of all the type arguments string names
     */
    public Variable(String type,  Pattern[] typesPatterns,
                    ArrayList<String> allowedTypesAssignment) {
        this.type = type;

        this.finalVariable = false;
        this.typesPatterns = typesPatterns;
        this.allowedTypesAssignment = allowedTypesAssignment;
        this.value = false;
    }

    public Variable(Variable other){
        this.type = other.type;
        this.finalVariable = other.finalVariable;
        this.typesPatterns = other.typesPatterns;
        this.allowedTypesAssignment = other.allowedTypesAssignment;
        this.value = other.value;
    }

    /**
     * get the type of the Variable
     * @return string type
     */
    public String getType() {
        return type;
    }


    /**
     * get the value of the Variable
     * @return boolean true or false value
     */
    public boolean getValue(){
        return value;
    }

    /**
     * this function sets the value of the Variable
     * @param value the value the Variable
     * @param map the map we want to save it
     */
    public void setValue(String value, HashMapVariable map) throws SjavacException{
        if (this.finalVariable){ // cant assign values to final variables
            throw new SjavacException(SjavacException.ASSIGN_TO_FINAL_ERR);
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
                throw new SjavacException(SjavacException.IN_NULL_OR_DIFF_TYPE_ERR +
                        "\n" + this.type + " : " + value);
            }
        } else { // if there is only outer scope variable - only then its deciding
            if (map.getOuterScope(value) != null  &&
                    this.allowedTypesAssignment.contains(map.getOuterScope(value).getType())) {
                if (map.getOuterScope(value).getValue()){
                    this.value = true;
                } else {
                    throw new SjavacException(SjavacException.OUT_NULL_OR_DIFF_TYPE_ERR +
                            "\n" + this.type + " : " + value);
                }
            } else {
                throw new SjavacException(SjavacException.INVALID_ASSIGN_ERR +
                        "\n" + this.type + " : " + value);
            }
        }
    }


    /**
     *  set the value of the Variable to true
     */
    public void setValueTrue(){
        this.value = true;
    }

    /**
     * we check the Variable declaration  is correct format
     * @param line the line itself
     * @param map the map we want to save it
     */
    public static void compileVariableDeclaration(String line, HashMapVariable map)
        throws SjavacException{
        String type;
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
            throw new SjavacException(SjavacException.INVALID_VAR_TYPE_ERR + line);
        }
        // check for variables
        line = addVariableFromLineStart(line, type, finalVariable, map);
        matcher = RegularExpressions.COMA_PATTERN.matcher(line);
        // if there is "," then there is another variable
        while (matcher.lookingAt()) {
            line = line.substring(matcher.end());
            line = addVariableFromLineStart(line, type, finalVariable, map);
            matcher = RegularExpressions.COMA_PATTERN.matcher(line);
        }
        matcher = RegularExpressions.COLON_PATTERN.matcher(line);
        if (!matcher.lookingAt()) {  // end of line must be ;
            throw new SjavacException(SjavacException.END_COLON_ERR);
        }
    }

    /**
     * get a line that was already compiled and the next 1 or 2 words should be a variable and an
     * assignment or a variable without an assignment
     * @param line the current line to compile
     * @param type the type of variable
     * @param finalVariable is this variable final
     * @return the line after the variable declaration
     */
    public static String addVariableFromLineStart(String line,
                                                  String type,
                                                  boolean finalVariable,
                                                  HashMapVariable map) throws SjavacException{
        // check type
        Variable variable = VariableFactory.createVariable(type);
        line = map.validatingName(line, variable, false);
        Matcher matcher = RegularExpressions.ASSIGN_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            variable.setValue(matcher.group(1), map);
            variable.setFinale(finalVariable); // set final to true only after value assignment
            line = line.substring(matcher.end());
        } else {
            if (finalVariable) { // if it's a final variable there must be assignment
                throw new SjavacException(SjavacException.UNASSIGNED_FINAL_ERR);
            }
        }
        return line;
    }

    /**
     * set the final Variable
     * @param finalVariable set the final Variable
     */
    public void setFinale(boolean finalVariable) {
        if (this.finalVariable){
            return;
        }
        this.finalVariable = finalVariable;
    }
}
