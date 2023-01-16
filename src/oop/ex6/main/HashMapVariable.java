package oop.ex6.main;
import java.util.HashMap;
import java.util.regex.Matcher;
import static oop.ex6.main.RegularExpressions.*;


public class HashMapVariable {

    private static final String[] ALLOWED_TYPES = {STRING, INT, CHAR, DOUBLE, BOOLEAN};
    private static final String[] VARIABLE_DECORATORS = {FINAL};
    // we save 2 maps, one for the current scope and one for the outer scope
    private final HashMap<String, Variable> currentScope;
    private final HashMap<String, Variable> outerScope;

    /**
     * con constructor begins both map's
     */
    public HashMapVariable() {
        currentScope = new HashMap<>();
        outerScope = new HashMap<>();
    }

    /**
     * if we want to construct a for a new HashMapVariable
     * @param other HashMapVariable
     */
    public HashMapVariable(HashMapVariable other) { // copy constructor
        currentScope = new HashMap<>();
        outerScope = new HashMap<>();
        for (String variableName : other.outerScope.keySet()){ // deep copy outer scope to outer scope
            this.outerScope.put(variableName, new Variable(other.outerScope.get(variableName)));
        }
        for (String variableName : other.currentScope.keySet()){ // deep copy other.inner scope to outer scope
            this.outerScope.put(variableName, new Variable(other.currentScope.get(variableName)));
        }
    }

    /**
     *  we check if the line starts with a type or final
     * @param line the line where currently checking
     * @return true or false
     */
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

    /**
     * checking if the name is saved in current scope
     * @param name the name we want to check
     * @return Variable
     */
    public Variable getCurrentScope(String name){ // throw VariableDoNotExist
        if (currentScope.containsKey(name)){
            return currentScope.get(name);
        }
        return null; // throw VariableDoNotExist
    }

    /**
     * checking if the name is saved in outer scope
     * @param name the name we want to check
     * @return Variable
     */
    public Variable getOuterScope(String name) {
        if (outerScope.containsKey(name)){
            return outerScope.get(name);
        }
        return null; // throw VariableDoNotExist
    }


    /**
     * puting in the current scope the name and its Variable
     * @param name th name of the argument
     * @param variable its Variable
     */
    public void putCurrentScope(String name, Variable variable){
        this.currentScope.put(name,variable);
    }

    /**
     * validating the name is current
     * @param line the line where checking
     * @param variable its Variable
     * @param globalRun to know if it's the first run or the second run
     * @return to know if its global run
     * @throws SjavacException throw an error if we need to
     */
    public String validatingName(String line, Variable variable, boolean globalRun)
        throws SjavacException{
        String name;
        Matcher matcher = RegularExpressions.VAR_NAME_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            name = matcher.group(1);
            // if the name exists already
            if (this.getCurrentScope(name) != null && !globalRun){
                throw new SjavacException(SjavacException.VAR_ALREADY_EXIST_ERR + name);
            }
            line = line.substring(matcher.end());
        } else {
            throw new SjavacException(SjavacException.INVALID_VAR_NAME_ERR + line);
        }
        if (!globalRun){
            this.putCurrentScope(name, variable);
        }
        return line;
    }
}
