package opp.ex6.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import static opp.ex6.main.RegularExpressions.*;

public class Scope {

    /**
     *
     * @param bufferedReader
     * @param currMap
     * @param methods
     * @return
     * @throws IOException
     */
    public static boolean compileScope(BufferedReader bufferedReader, HashMapVariable currMap,
                HashMap<String, ArrayList<String>> methods) throws IOException {
        String line;
        boolean lastReturn = false;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("//")){continue;}
            line = line.trim();
            if (line.equals("")) {continue;}
            if (line.startsWith("}") && line.substring(1).trim().equals("")){
                return lastReturn;
            } else {
                lastReturn = RegularExpressions.RETURN_LINE_PATTERN.matcher(line).matches();
                if (lastReturn) {
                    // check if
                    continue;
                }
                if (line.startsWith(IF) || line.startsWith(WHILE)) {
                    compileIfWhile(line, bufferedReader, currMap, methods);
                } else if (HashMapVariable.isLineVariableDeclaration(line)) {
                    Variable.compileVariableDeclaration(line, false, currMap);
                } else {
                    Matcher matcher = RegularExpressions.IS_METHOD_CALL_PATTERN.matcher(line);
                    if (matcher.lookingAt()) { // it's a method call
                        Method.compileMethodCall(line, currMap, methods);
                    } else { // assumes it's an assignment
                        compileAssignment(line, currMap);
                    }
                }
            }
        }
        System.out.println("raise error: end of file reached with no end of scope");
        return false;
    }

    /**
     *
     * @param line
     * @param bufferedReader
     * @param map
     * @param methods
     * @throws IOException
     */
    private static void compileIfWhile(String line, BufferedReader bufferedReader, HashMapVariable map,
                                       HashMap<String, ArrayList<String>> methods)
            throws IOException { // throws InvalidValue / WrongSyntax
        HashMapVariable currMap = new HashMapVariable(map);
        Variable var = null;
        Matcher matcher = RegularExpressions.WHILE_IF_PATTERN.matcher(line);
        if(matcher.matches()){
            String condition = matcher.group(1);
            while (!condition.equals("")){
                condition = condition.trim();
                matcher = RegularExpressions.ALL_BOOLEAN_PATTERN.matcher(condition);
                if (matcher.lookingAt()){ // if its a constant valid boolean
                    condition = condition.substring(matcher.end());
                } else { // assume it's a variable name
                    matcher = RegularExpressions.VAR_NAME_PATTERN.matcher(condition);
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
        compileScope(bufferedReader, currMap, methods);
    }

    /**
     *
     * @param line
     * @param map
     */
    public static void compileAssignment(String line, HashMapVariable map) { // throws InvalidValue / WrongSyntax
        Matcher matcher = VALID_ASSIGNMENT_PATTERN.matcher(line);
        if (matcher.matches()) {
            line = handlingCompileAssignment(line, map);
            matcher = COMA_PATTERN.matcher(line);
            while (matcher.lookingAt()) {
                line = line.substring(matcher.end());
                line = handlingCompileAssignment(line, map);
                matcher = COMA_PATTERN.matcher(line);
            }
            matcher = RegularExpressions.COLON_PATTERN.matcher(line);
            if (!matcher.lookingAt()) {  // end of line must be ;
                System.out.println("raise error: line must end with ;");
            }
        }else {
            System.out.println("raise error: line does not fit the syntax");
        }
    }

    /**
     *
     * @param line
     * @param map
     * @return
     */
    private static String handlingCompileAssignment(String line, HashMapVariable map){
        Variable variable = null;
        Matcher matcher = RegularExpressions.VAR_NAME_PATTERN.matcher(line);
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
        matcher = RegularExpressions.ASSIGN_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            assert variable != null;
            variable.setValue(matcher.group(1), map);
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error: assignment syntax error" + line);
        }
        return line;
    }
}
