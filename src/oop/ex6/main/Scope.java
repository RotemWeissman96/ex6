package oop.ex6.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class Scope {

    /**
     * this is where we compile the scope of the function
     * @param bufferedReader the buffer where will get all the lines from the file
     * @param currMap the map where will keep all the arguments
     * @param methods Arraylist that contain the function name and all of its arguments
     * @return true or false
     */
    public static boolean compileScope(BufferedReader bufferedReader, HashMapVariable currMap,
                HashMap<String, ArrayList<String>> methods) throws IOException, SjavacException {
        String line;
        boolean lastReturn = false;
        while ((line = bufferedReader.readLine()) != null) {
            //it's "//" we skip over it
            if (line.startsWith(RegularExpressions.DOUBLE_LINES)){continue;}
            line = line.trim();
            if (line.equals(RegularExpressions.EMPTY)) {continue;}
            if (line.startsWith(RegularExpressions.CLOSE_CURLY_BRACKETS) && line.substring(1).trim().equals("")){
                return lastReturn;
            } else {
                // if it's the return value
                lastReturn = RegularExpressions.RETURN_LINE_PATTERN.matcher(line).matches();
                if (lastReturn) {
                    continue;
                }
                // checks it contains "if" or "while"
                if (line.startsWith(RegularExpressions.IF) || line.startsWith(RegularExpressions.WHILE)) {
                    compileIfWhile(line, bufferedReader, currMap, methods);
                } else if (HashMapVariable.isLineVariableDeclaration(line)) {
                    Variable.compileVariableDeclaration(line, currMap);
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
        throw new SjavacException(SjavacException.NO_END_SCOPE_ERR);
    }

    /**
     * checks "if" it and "while conditions
     * @param line the current line in the file
     * @param bufferedReader the buffer where will get all the lines from the file
     * @param map the map where we kept all the arguments
     * @param methods Arraylist that contain the function name and all of its elements
     * @throws IOException, SjavacException
     */
    private static void compileIfWhile(String line, BufferedReader bufferedReader, HashMapVariable map,
                                       HashMap<String, ArrayList<String>> methods)
            throws IOException, SjavacException { // throws InvalidValue / WrongSyntax
        HashMapVariable currMap = new HashMapVariable(map);
        Matcher matcher = RegularExpressions.WHILE_IF_PATTERN.matcher(line);
        if(matcher.matches()){ // make sure the if/while syntax is correct
            String condition = matcher.group(1);
            condition = checkValidConditionArgument(condition, map);
            while (!condition.equals(RegularExpressions.EMPTY)){
                matcher = RegularExpressions.AND_OR_PATTERN.matcher(condition);
                matcher.lookingAt();
                condition = condition.substring(matcher.end());
                condition = checkValidConditionArgument(condition, map);
            }
        } else {
            throw new SjavacException(SjavacException.IF_WHILE_SYNTAX_ERR + line);
        }
        compileScope(bufferedReader, currMap, methods);
    }

    /**
     * checks the valid condition
     * @param condition the condition that will check
     * @param map the map where we kept all the arguments
     * @return the condition
     */
    private static String checkValidConditionArgument(String condition, HashMapVariable map)
            throws SjavacException {
        Variable var;
        condition = condition.trim();
        Matcher matcher = RegularExpressions.ALL_BOOLEAN_PATTERN.matcher(condition);
        if (matcher.lookingAt()){ // if it's a constant valid boolean
            condition = condition.substring(matcher.end());
        } else { // assume it's a variable name
            matcher = RegularExpressions.VAR_NAME_PATTERN.matcher(condition);
            if (matcher.lookingAt()) {
                if ((var = map.getCurrentScope(matcher.group(1))) == null) { // first check local
                    if ((var = map.getOuterScope(matcher.group(1))) == null) { // then check outer
                        throw new SjavacException(SjavacException.VAR_NOT_EXIST_ERR + matcher.group(1));
                    }
                }
                if (VariableFactory.BOOLEAN_VALID_TYPES.contains(var.getType()) && var.getValue()){
                    //check if the type is boolean friendly and that the var was initiated
                    condition = condition.substring(matcher.end()).trim();
                } else {
                    throw new SjavacException(SjavacException.VAR_IS_NULL_ERR + matcher.group(1));
                }
            } else {
                throw new SjavacException(SjavacException.INVALID_VAR_NAME_ERR + condition);
            }
        }
        return condition;
    }


    /**
     * this function deals with assignment in the function we are reading from and making sure its current
     * @param line the current line in the file
     * @param map the map where we kept all the arguments
     */
    public static void compileAssignment(String line, HashMapVariable map) throws SjavacException {
        Matcher matcher = RegularExpressions.VALID_ASSIGNMENT_PATTERN.matcher(line);
        if (matcher.matches()) {
            line = handlingCompileAssignment(line, map);
            matcher = RegularExpressions.COMA_PATTERN.matcher(line);
            while (matcher.lookingAt()) {
                line = line.substring(matcher.end());
                line = handlingCompileAssignment(line, map);
                matcher = RegularExpressions.COMA_PATTERN.matcher(line);
            }
            matcher = RegularExpressions.COLON_PATTERN.matcher(line);
            if (!matcher.lookingAt()) {  // end of line must be ;
                throw new SjavacException(SjavacException.END_COLON_ERR);
            }
        }else {
            throw new SjavacException(SjavacException.SYNTAX_ERR + line);
        }
    }

    /**
     * checks the name of the assignment
     * @param line the current line in the file
     * @param map the map where we kept all the arguments
     * @return the line where at
     */
    private static String handlingCompileAssignment(String line, HashMapVariable map) throws SjavacException{
        Variable variable;
        Matcher matcher = RegularExpressions.VAR_NAME_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            if((variable = map.getCurrentScope(matcher.group(1))) == null) {
                if ((variable = map.getOuterScope(matcher.group(1))) == null) {
                    throw new SjavacException(SjavacException.VAR_NOT_EXIST_ERR + matcher.group(1));
                }
            }
            line = line.substring(matcher.end());
        } else {
            throw new SjavacException(SjavacException.VAR_NOT_EXIST_ERR + line);
        }
        matcher = RegularExpressions.ASSIGN_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            variable.setValue(matcher.group(1), map);
            line = line.substring(matcher.end());
        } else {
            throw new SjavacException(SjavacException.SYNTAX_ERR + line);
        }
        return line;
    }
}
