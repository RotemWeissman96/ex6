package oop.ex6.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import static java.lang.Boolean.FALSE;

public class Method {
    private final HashMap<String, ArrayList<String>> methods;

    /**
     * the constructor for Method
     * @param methods Arraylist that contain the function name and all of its elements
     */
    public Method(HashMap<String, ArrayList<String>> methods){
        this.methods = methods;
    }

    /**
     * this function will run throw the whole file void function
     * @param bufferedReader the buffer where will get all the lines from the file
     * @param map the map where we kept all the global arguments
     * @param line the current line in the file
     * @throws IOException, SjavacException
     */
    public void compileMethodBody(BufferedReader bufferedReader, HashMapVariable map, String line)
            throws IOException, SjavacException {
        // creates a new map for the current scope
        HashMapVariable currMap = new HashMapVariable(map);
        compileMethodHead(currMap,line,FALSE);
        if(!Scope.compileScope(bufferedReader,currMap, methods)){
            throw new SjavacException(SjavacException.METHOD_NO_RETURN_ERR);
        }

    }

    /**
     *
     * @param currMap the map where will keep all the arguments
     * @param line the current line in the file
     * @param globalRun a boolean to know its it's in the global run or the function run
     * @throws SjavacException throws an exception when needed
     */
    private void compileMethodHead( HashMapVariable currMap, String line, boolean globalRun)
            throws SjavacException { // throws InvalidValue / WrongSyntax
        String functionName;
        // create a ArrayList saving all the local arguments of this function
        ArrayList<String> functionArguments = new ArrayList<>();
        Matcher matcher = RegularExpressions.VOID_PATTERN.matcher(line);
        // checking that the void has space from the name
        if (matcher.lookingAt()) {
            line = line.substring(matcher.end());
        } else {
            throw new SjavacException(SjavacException.VOID_SPACING_ERR);
        }
        //check if the name of the function is current
        matcher = RegularExpressions.FUNCTION_NAME_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            functionName =  matcher.group(1);
            line = line.substring(matcher.end());
        } else {
            throw new SjavacException(SjavacException.INVALID_FUNCTION_NAME_ERR + line);
        }

        // if the function is without arguments at all
        matcher = RegularExpressions.ENDING_HEAD_FUN_PATTERN.matcher(line);
        if(matcher.matches()){
            if(globalRun){
                methods.put(functionName,functionArguments);
            }
            return;
        }
        line = saveArgumentCallingFunction(line, functionArguments, currMap, globalRun);

        // if it's the global run we want to keep it in the methods map
        if(globalRun){
            methods.put(functionName,functionArguments);
        }
        // checking for the end with a "{"
        matcher = RegularExpressions.ENDING_HEAD_FUN_PATTERN.matcher(line);
        if(!matcher.matches()){
            throw new SjavacException(SjavacException.FUN_HEAD_ERR + line);
        }
    }

    /**
     * getting all the elements when we first call the function
     * @param line the current line in the file
     * @param functionArguments a ArrayList saving all the local arguments
     * @param currMap the map where will keep all the arguments
     * @param globalRun  a boolean to know its it's in the global run or the function run
     * @return the line
     */
    private static String saveArgumentCallingFunction(String line, ArrayList<String> functionArguments,
                                                      HashMapVariable currMap, Boolean globalRun)
                                                    throws  SjavacException{
        boolean finalVariable = false;
        line = checkingMethodArgument(functionArguments,line,currMap,finalVariable, globalRun);
        Matcher matcher = RegularExpressions.NEXT_ARGUMENT_PATTERN.matcher(line);
        // running a loop getting all the arguments of the function
        while (matcher.lookingAt()) {
            line = line.substring(matcher.end());
            line = checkingMethodArgument(functionArguments,line,currMap,finalVariable, globalRun);
            matcher = RegularExpressions.NEXT_ARGUMENT_PATTERN.matcher(line);
        }
        return line;
    }

    /**
     *
     * @param functionArguments a ArrayList saving all the local arguments
     * @param line the current line in the file
     * @param currMap the map where will keep all the arguments
     * @param finalVariable a boolean to know if the argument is final
     * @param globalRun a boolean to know its it's in the global run or the function run
     * @return String of the line
     */
    private static String checkingMethodArgument(ArrayList<String> functionArguments,
                                          String line,
                                          HashMapVariable currMap,
                                          Boolean finalVariable,
                                          boolean globalRun) throws SjavacException{
        // checking if there is a final
        Matcher matcher = RegularExpressions.FINAL_PATTERN.matcher(line);
        if (matcher.lookingAt()){
            finalVariable = true;
            line = line.substring(matcher.end());
        }
        // checking the type of the argument
        matcher = RegularExpressions.TYPE_PATTERN.matcher(line);
        if(!matcher.lookingAt()) {
            throw new SjavacException(SjavacException.INVALID_VAR_TYPE_ERR + line);
        }
        // creating a variable and adding it to the map
        String type = matcher.group(1);
        Variable variable = VariableFactory.createVariable(type);
        variable.setFinale(finalVariable);
        variable.setValueTrue();
        functionArguments.add(type);
        line = line.substring(matcher.end());
        return currMap.validatingName(line, variable, globalRun);
    }

    /**
     * this function we use in the first time that we ran throw the file we check the compileMethodHead
     * and skip the rest of the function
     * @param bufferedReader the buffer where will get all the lines from the file
     * @throws IOException, SjavacException
     */
    public void SaveAndSkipMethod(BufferedReader bufferedReader, HashMapVariable map, String line)
            throws IOException, SjavacException {
        compileMethodHead(map,line,true);
        int countBrackets = 1;
        while (countBrackets != 0 && (line = bufferedReader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith(RegularExpressions.CLOSE_CURLY_BRACKETS) && line.substring(1).trim().equals(RegularExpressions.EMPTY)) {
                countBrackets -= 1;
            } else if (line.startsWith(RegularExpressions.IF) || line.startsWith(RegularExpressions.WHILE)) {
                countBrackets += 1;
            }
        }
        if(line == null) {
            throw new SjavacException(SjavacException.SCOPE_NOT_CLOSED_ERR);
        }
    }

    /**
     * this function wwe call a function within a void and check that is currect
     * @param line the current line in the file
     * @param map the map where we kept all the global arguments
     * @param methods Arraylist that contain the function name and all of its elements
     */
    public static void compileMethodCall(String line, HashMapVariable map,
                                          HashMap<String, ArrayList<String>> methods) throws SjavacException{
        // checking the function name
        Matcher matcher = RegularExpressions.FUNCTION_NAME_PATTERN.matcher(line);
        if (matcher.lookingAt()){
            String argumentList = line.substring(matcher.end());
            ArrayList<String> argumentsType = methods.get(matcher.group(1));
            if (argumentsType != null) {
                for (String type : argumentsType) {
                    matcher = RegularExpressions.ARGUMENT_PATTERN.matcher(argumentList);
                    if (matcher.lookingAt()){
                        Variable testVar = VariableFactory.createVariable(type);
                        testVar.setValue(matcher.group(1), map);
                        argumentList = argumentList.substring(matcher.end());
                    } else {
                        throw new SjavacException(SjavacException.INVALID_ARG_LIST_ERR + line);
                    }
                    matcher = RegularExpressions.COMA_PATTERN.matcher(argumentList);
                    if (matcher.lookingAt()){
                        argumentList = argumentList.substring(matcher.end());
                    }
                }
                matcher = RegularExpressions.END_METHOD_CALL_PATTERN.matcher(argumentList);
                if (!matcher.matches()) {
                    throw new SjavacException(SjavacException.NOT_ENOUGH_ARGS_ERR);
                }
            } else {
                throw new SjavacException(SjavacException.METHOD_NAME_CALL_ERR + matcher.group(1));
            }
        } else {
            throw new SjavacException(SjavacException.INVALID_FUNCTION_NAME_ERR + line);
        }
    }
}
