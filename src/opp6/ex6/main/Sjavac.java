package opp6.ex6.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static opp6.ex6.main.RegularExpressions.*;

//test
public class Sjavac {

    private static final HashMap<String, ArrayList<String>> methods = new HashMap<>();
    public static void main(String[] args) {
        try {
            HashMapVariable map = new HashMapVariable();
            globalSearch(args[1], map);
            map.printMaps();
            functionsSearch(args[1], map);
        } catch (IOException e) {
            e.printStackTrace(); // print 2
        } //TODO: catch InvalidValue / WrongSyntax
    }

    private static void globalSearch(String path, HashMapVariable map)
            throws IOException { // throws InvalidValue / WrongSyntax
        String line;
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.equals("") || line.contains("//")) { // if line was all comma or an empty line
                    continue;
                }
                if (line.startsWith("void")) {
                    skipMethod(bufferedReader);
                    continue;
                }
                if (HashMapVariable.isLineVariableDeclaration(line)) {  // check if its a global declaration
                    compileVariableDeclaration(line, true, map);
                    continue;
                }
                compileAssignment(line, map);
                // if it's not a declaration and not a method, assume it's an assign
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void functionsSearch(String path, HashMapVariable map) {  // throws InvalidValue / WrongSyntax
        String line;
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(VOID)) {
                    compileMethod(bufferedReader, map,line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compileVariableDeclaration(String line, boolean global, HashMapVariable map) {
        String type = null;
        boolean finalVariable = false;
        // check final
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
            System.out.println("raise error: that variable type does not exist");
        }
        // check for variables
        line = addVariableFromLineStart(line, type, global, finalVariable, map);
        matcher = COMA_PATTERN.matcher(line);
        // if there is "," then there is another variable
        while (matcher.lookingAt()) {
            line = line.substring(matcher.end());
            line = addVariableFromLineStart(line, type, global, finalVariable, map);
            matcher = COMA_PATTERN.matcher(line);
        }
        matcher = COLON_PATTERN.matcher(line);
        if (!matcher.lookingAt()) {  // end of line must be ;
            System.out.println("raise error: line must end with ;");
        }
    }

    private static boolean compileScope(BufferedReader bufferedReader, HashMapVariable currMap) throws IOException{
        String line;
        boolean lastReturn = false;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("}") && line.substring(1).trim().equals("")){
                currMap.printMaps();
                return lastReturn;
            } else {
                lastReturn = RETURN_LINE_PATTERN.matcher(line).matches(); // check if
                if (line.startsWith("if") || line.startsWith("while")) {
                    compileIfWhile(line, bufferedReader, currMap);
                } else if (HashMapVariable.isLineVariableDeclaration(line)) {
                    compileVariableDeclaration(line, false, currMap);
                } else { // assumes it's an assignment
                    compileAssignment(line, currMap);
                }
            }
        }
        System.out.println("raise error: end of file reached with no end of scope");
        return false;
    }

    private static void compileMethod(BufferedReader bufferedReader, HashMapVariable map, String line)
            throws IOException { // throws InvalidValue / WrongSyntax
        HashMapVariable currMap = new HashMapVariable(map);
        String functionName = null, type = null;
        Matcher matcher = VOID_PATTERN.matcher(line);
        // checking that the void has space from the name
        if (matcher.lookingAt()) {
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error: the void does not have space from the name");
        }
        //check  if the name is like is supposed to be
        matcher = FUNCTION_NAME_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            functionName =  matcher.group(1);
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error: the function name was not correct");
        }
        ArrayList<String> functionArguments = new ArrayList<>();
        matcher = TYPE_PATTERN.matcher(line);
        while (matcher.lookingAt()) {
            type = matcher.group(1);
            Variable variable = VariableFactory.createVariable(type, false);
            functionArguments.add(type);
            line = line.substring(matcher.end());
            line = validatingName(line, currMap, variable);

            matcher = NEXT_ARGUMENT_PATTERN.matcher(line);
        }
        methods.put(functionName,functionArguments);
        matcher = ENDING_SCOPE_PATTERN.matcher(line);
        if(!matcher.matches()){
            System.out.println("the function dos not end well");
        }
        if(!compileScope(bufferedReader,currMap)){
            System.out.println("there wasn't a closing bracket");
        }
    }

    private static void compileIfWhile(String line, BufferedReader bufferedReader, HashMapVariable map)
            throws IOException { // throws InvalidValue / WrongSyntax
        HashMapVariable currMap = new HashMapVariable(map);
        Variable var = null;
        Matcher matcher = WHILE_IF_PATTERN.matcher(line);
        if(matcher.matches()){
            String condition = matcher.group(1);
            while (!condition.equals("")){
                condition = condition.trim();
                matcher = ALL_BOOLEAN_PATTERN.matcher(condition);
                if (matcher.lookingAt()){ // if its a constant valid boolean
                    condition = condition.substring(matcher.end());
                } else { // assume it's a variable name
                    matcher = VAR_NAME_PATTERN.matcher(condition);
                    if (matcher.lookingAt()) {
                        if ((var = map.getCurrentScope(matcher.group(1))) == null) { // first check local
                            if ((var = map.getOuterScope(matcher.group(1))) == null) { // then check outer
                                System.out.println("raise error: this variable does not exist: " + matcher.group(1));
                                return;
                            }
                        }
                        if (!VariableFactory.BOOLEAN_VALID_TYPES.contains(var.getType()) || !var.getValue()){
                            //check if the type is boolean friendly and that the var was initiated
                                System.out.println("raise error: this variable is not a initialized " +
                                        "boolean: " + matcher.group(1));
                        }
                    } else {
                        System.out.println("raise error: this variable name is not valid: " + condition);
                    }
                }
            }
        } else {
            System.out.println("raise error: wrong if/while syntax: " + line);
        }
        compileScope(bufferedReader, currMap);
    }

    /**
     *
     * @param line
     * @param map
     */
    private static void compileAssignment(String line, HashMapVariable map) { // throws InvalidValue / WrongSyntax
        line = handlingCompileAssignment(line, map);
        Matcher matcher = COMA_PATTERN.matcher(line);
        while (matcher.lookingAt()){
            line = line.substring(matcher.end());
            line = handlingCompileAssignment(line, map);
            matcher = COMA_PATTERN.matcher(line);
        }
        matcher = COLON_PATTERN.matcher(line);
        if (!matcher.lookingAt()) {  // end of line must be ;
            System.out.println("raise error: line must end with ;");
        }
    }

    private static String handlingCompileAssignment(String line, HashMapVariable map){
        Variable variable = null;
        Matcher matcher = VAR_NAME_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            if((variable = map.getCurrentScope(matcher.group(1))) == null) {
                if ((variable = map.getOuterScope(matcher.group(1))) == null) {
                    System.out.println("raise error: this variable was not declared: " + matcher.group(1));
                }
            }
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error: that is not a valid variable name: " + matcher.group(1));
        }
        matcher = ASSIGN_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            assert variable != null;
            variable.setValue(matcher.group(1), map);
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error: assignment syntax error" + line);
        }
        return line;
    }


    /**
     *
     * @param bufferedReader
     * @throws IOException
     */
    private static void skipMethod(BufferedReader bufferedReader) throws IOException {

        int countBrackets = 1;
        String line = null;
        while (countBrackets != 0 && (line = bufferedReader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("}") && line.substring(1).trim().equals("")) {
                countBrackets -= 1;
            } else if (line.startsWith(IF) || line.startsWith(WHILE)) {
                countBrackets += 1;
            }
        }
        if(line == null) {
            System.out.println("raise error: some scope was not closed");
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
        line = validatingName(line, map, variable);
        Matcher matcher = ASSIGN_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            variable.setValue(matcher.group(1), map);
            variable.setFinale(finalVariable); // set final to true only after value assignment
            line = line.substring(matcher.end());
        } else {
            if (finalVariable) { // if it's a final variable there must be assignment
                System.out.println("raise error 1");
            }
        }
        return line;
    }

    private static String validatingName(String line,  HashMapVariable map, Variable variable){
        String name = "";
        Matcher matcher = VAR_NAME_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            name = matcher.group(1);
            if (map.getCurrentScope(name) != null){
                System.out.println("raise error: this variable name is already used in this scope: " + name);
            }
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error: this is not a valid variable name: " + line);
        }
        map.putCurrentScope(name, variable);
        return line;
    }

}

