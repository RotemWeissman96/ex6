package opp6.ex6.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sjavac {


    public static void main(String[] args) {
        try {
            HashMapVariable map = new HashMapVariable();
            globalSearch(args[1], map);
            functionsSearch(args[1], map);
        }
        catch (IOException e) {
            e.printStackTrace(); // print 2
        } //TODO: catch InvalidValue / WrongSyntax
    }

    private static void globalSearch(String path, HashMapVariable map)
            throws IOException{ // throws InvalidValue / WrongSyntax
        String line;
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader) ){
            while ((line = bufferedReader.readLine()) != null) {
                line = removeSpacesAndComma(line);
                if (line.equals("")){ // if line was all comma or an empty line
                    continue;
                }
                if (line.startsWith("void")){
                    skipMethod(bufferedReader);
                }
                if (HashMapVariable.isLineVariableDeclaration(line)){  // check if its a global declaration
                    compileVariableDeclaration(line, true);
                }
                //TODO: check if this line is an assignment (without declaration) and call the function
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compileVariableDeclaration(String line, boolean global) {
        //TODO: handle a variable declaration. assumes that the first word is final or a valid type
        //TODO: use HashMapVariable.createVariable function
    }

    private static void functionsSearch(String path, HashMapVariable map) {  // throws InvalidValue / WrongSyntax
        String line;
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader) ){
            while ((line = bufferedReader.readLine()) != null) {
                line = removeSpacesAndComma(line);
                if (line.equals("")){ // if line was all comma or an empty line
                    continue;
                }
                if (line.startsWith("void")){
                    compileMethod(bufferedReader, map);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compileMethod(BufferedReader bufferedReader, HashMapVariable map)
            throws IOException { // throws InvalidValue / WrongSyntax
        HashMapVariable currMap = new HashMapVariable(map);
        //TODO: compile a method
    }

    private static void compileIfWhile(BufferedReader bufferedReader, HashMapVariable map)
            throws IOException { // throws InvalidValue / WrongSyntax
        HashMapVariable currMap = new HashMapVariable(map);
        //TODO: compile a while/if statement
    }

    private static void compileAssignment(String line, HashMapVariable map){ // throws InvalidValue / WrongSyntax
        //TODO: compile assignment, check if variable exist in current scope, use Variable.setValue function
        //TODO: handle an uninitialized global or outer scope variable as a new variable in current scope
    }

    private static void skipMethod(BufferedReader bufferedReader) throws IOException {
        //TODO: write a function that skips a method - from void....{    to the last return;}
    }

    private static String removeSpacesAndComma(String line) {
        //TODO: write a function to remove unnecessary spaces from the beginning of line and commas
        return line;
    }
}
