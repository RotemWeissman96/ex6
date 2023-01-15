package opp.ex6.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class Method {
    private final HashMap<String, ArrayList<String>> methods;

    public Method(HashMap<String, ArrayList<String>> methods){
        this.methods = methods;
    }

    public void compileMethodBody(BufferedReader bufferedReader, HashMapVariable map, String line) throws IOException {
        HashMapVariable currMap = new HashMapVariable(map);
        compileMethodHead(currMap,line,false);
        if(!Scope.compileScope(bufferedReader,currMap, methods)){
            System.out.println("raise error: this method ended with } without doing return;");
        }

    }

    private void compileMethodHead( HashMapVariable currMap, String line, boolean globalRun)
            throws IOException { // throws InvalidValue / WrongSyntax
        String functionName = null;
        Matcher matcher = RegularExpressions.VOID_PATTERN.matcher(line);
        // checking that the void has space from the name
        if (matcher.lookingAt()) {
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error: the void does not have space from the name");
        }
        //check  if the name is like is supposed to be
        matcher = RegularExpressions.FUNCTION_NAME_PATTERN.matcher(line);
        if (matcher.lookingAt()) {
            functionName =  matcher.group(1);
            line = line.substring(matcher.end());
        } else {
            System.out.println("raise error: the function name was not correct");
        }
        ArrayList<String> functionArguments = new ArrayList<>();
        boolean finalVariable = false;
        line = checkingMethodArgument(functionArguments,line,currMap,finalVariable, globalRun);
        matcher = RegularExpressions.NEXT_ARGUMENT_PATTERN.matcher(line);
        while (matcher.lookingAt()) {
            line = line.substring(matcher.end());
            line = checkingMethodArgument(functionArguments,line,currMap,finalVariable, globalRun);
            matcher = RegularExpressions.NEXT_ARGUMENT_PATTERN.matcher(line);
        }
        if(globalRun){
            methods.put(functionName,functionArguments);
        }
        matcher = RegularExpressions.ENDING_SCOPE_PATTERN.matcher(line);
        if(!matcher.matches()){
            System.out.println("the function dos not end well");
        }
    }

    private String checkingMethodArgument(
            ArrayList<String> functionArguments, String line,
            HashMapVariable currMap, Boolean finalVariable, boolean globalRun){
        Matcher matcher = RegularExpressions.FINAL_PATTERN.matcher(line);
        if (matcher.lookingAt()){
            finalVariable = true;
            line = line.substring(matcher.end());
        }
        matcher = RegularExpressions.TYPE_PATTERN.matcher(line);
        if(!matcher.lookingAt()) {
            System.out.println("raise error: that variable type does not exist");
        }
        String type = matcher.group(1);
        Variable variable = VariableFactory.createVariable(type, false);
        variable.setFinale(finalVariable);
        variable.setValueTrue();
        functionArguments.add(type);
        line = line.substring(matcher.end());
        return currMap.validatingName(line, variable, globalRun);
    }

    /**
     *
     * @param bufferedReader
     * @throws IOException
     */
    public void SaveAndSkipMethod(BufferedReader bufferedReader, HashMapVariable map, String line)
            throws IOException {
        compileMethodHead(map,line,true);
        int countBrackets = 1;
        while (countBrackets != 0 && (line = bufferedReader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("}") && line.substring(1).trim().equals("")) {
                countBrackets -= 1;
            } else if (line.startsWith(RegularExpressions.IF) || line.startsWith(RegularExpressions.WHILE)) {
                countBrackets += 1;
            }
        }
        if(line == null) {
            System.out.println("raise error: some scope was not closed");
        }
    }

    public static void compileMethodCall(String line, HashMapVariable map,
                                          HashMap<String, ArrayList<String>> methods){
        Matcher matcher = RegularExpressions.FUNCTION_NAME_PATTERN.matcher(line);
        if (matcher.lookingAt()){

            String argumentList = line.substring(matcher.end());
            ArrayList<String> argumentsType = methods.get(matcher.group(1));
            if (argumentsType != null) {
                for (String type : argumentsType) {
                    matcher = RegularExpressions.ARGUMENT_PATTERN.matcher(argumentList);
                    if (matcher.lookingAt()){
                        Variable testVar = VariableFactory.createVariable(type, false);
                        testVar.setValue(matcher.group(1), map);
                        argumentList = argumentList.substring(matcher.end());
                    } else {
                        System.out.println("raise error: this is not a valid argument list: " + line);
                    }
                    matcher = RegularExpressions.COMA_PATTERN.matcher(argumentList);
                    if (matcher.lookingAt()){
                        argumentList = argumentList.substring(matcher.end());
                    }
                }
                matcher = RegularExpressions.END_METHOD_CALL_PATTERN.matcher(argumentList);
                if (!matcher.matches()) {
                    System.out.println("raise error: there are to few arguments or end of line out of order");
                }
            } else {
                System.out.println("raise error: there is no method with that name: " + matcher.group(1));
            }
        } else {
            System.out.println("raise error: this is not a valid function name");
        }
    }
}
