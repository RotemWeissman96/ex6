package opp6.ex6.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;

import static opp6.ex6.main.RegularExpressions.*;

//test
public class Sjavac {


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
                    compileMethod(bufferedReader, map);
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
        line = map.addVariableFromLineStart(line, type, global, finalVariable);
        matcher = COMA_PATTERN.matcher(line);
        // if there is "," then there is another variable
        while (matcher.lookingAt()) {
            line = line.substring(matcher.end());
            line = map.addVariableFromLineStart(line, type, global, finalVariable);
            matcher = COMA_PATTERN.matcher(line);
        }
        matcher = COLON_PATTERN.matcher(line);
        if (!matcher.lookingAt()) {  // end of line must be ;
            System.out.println("raise error: line must end with ;");
        }
    }

    private static void compileScope(BufferedReader bufferedReader, HashMapVariable currMap) throws IOException{
        String line;
        while ((line = bufferedReader.readLine()) != null) { //TODO: add condition end of scope
            line = line.trim();
            if (line.startsWith("if") || line.startsWith("while")){
                compileIfWhile(line, bufferedReader, currMap);
            } else if (HashMapVariable.isLineVariableDeclaration(line)) {
                compileVariableDeclaration(line, false, currMap);
            } else { // assumes it's an assignment
                compileAssignment(line, currMap);
            }
        }
    }

    private static void compileMethod(BufferedReader bufferedReader, HashMapVariable map)
            throws IOException { // throws InvalidValue / WrongSyntax
        HashMapVariable currMap = new HashMapVariable(map);
        //TODO: compile a method
    }

    private static void compileIfWhile(String line, BufferedReader bufferedReader, HashMapVariable map)
            throws IOException { // throws InvalidValue / WrongSyntax
        HashMapVariable currMap = new HashMapVariable(map);
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
                        Variable var = map.getCurrentScope(matcher.group(1)); // first check local
                        if (var != null){
                            if (!VariableFactory.BOOLEAN_VALID_TYPES.contains(var.getType())){
                                System.out.println("raise error: this variable is not boolean: " + matcher.group(1));
                            }
                        } else { // only if none local then check for an outer scope variable
                            var = map.getOuterScope(matcher.group(1));
                            if (!VariableFactory.BOOLEAN_VALID_TYPES.contains(var.getType())){
                                System.out.println("raise error: this variable is not boolean: " + matcher.group(1));
                            }
                        }
                    } else {
                        System.out.println("raise error: this variable is not valid: " + condition);
                    }
                }
            }
        } else {
            System.out.println("raise error: wrong if/while syntax: " + line);
        }
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
        //TODO: compile assignment, check if variable exist in current scope, use Variable.setValue function
        //TODO: handle an uninitialized global or outer scope variable as a new variable in current scope
        //TODO: add a closing line condition (for ;) and a raise error print
        //TODO: if this line is not an assignment then its an error because its the last option to check
    }

    private static String handlingCompileAssignment(String line, HashMapVariable map){
        Variable localVariable = null, globalVariable = null, variable = null;
        String name = null;
        Matcher matcher = VAR_NAME_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            name = matcher.group(1);
            localVariable = map.getCurrentScope(name);
            globalVariable = map.getOuterScope(name);
            if(localVariable != null){
                variable = localVariable;
            }else {
                variable = globalVariable;
            }
            if(variable == null){
                System.out.println("raise error: this variable was not declared: " + name);
            }
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error: that is not a valid variable name: " + line);
        }
        matcher = ASSIGN_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            assert variable != null;
            variable.setValue(matcher.group(1), map);
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error: assignment syntax error" + matcher.group(0));
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
}

