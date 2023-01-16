package oop.ex6.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


//test
public class Sjavac {
    private static final HashMap<String, ArrayList<String>> methods = new HashMap<>();

    /***
     * the main we run through the file we got and checking that is compile for sjavac
     * @param args the args that we get in the command line in this case it will be the file will check
     */
    public static void main(String[] args) {
        try {
            HashMapVariable map = new HashMapVariable();
            // we run first time on the file to get all the global argument and to know all the functions
            globalSearch(args[0], map);
            // the second time we run throw the file we also go to the function search
            functionsSearch(args[0], map);
            System.out.println(0);
        } catch (IOException e) {
            System.out.println(2);
            e.printStackTrace(); // print 2
        } catch (SjavacException e) {
            System.out.println(1);
            e.printStackTrace();
        }
    }

    /**
     * we ran on the file to fin=d all the global arguments
     * @param path the path where the file is currently
     * @param map a hash map to save all the global arguments
     * @throws IOException, SjavacException
     */
    private static void globalSearch(String path, HashMapVariable map)
            throws IOException, SjavacException { // throws InvalidValue / WrongSyntax
        String line;
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith(RegularExpressions.DOUBLE_LINES)) {continue;}
                // trim the line in order to get the first argument without the spaces
                line = line.trim();
                if (line.equals(RegularExpressions.EMPTY)) {continue;}
                if (line.startsWith(RegularExpressions.VOID)) {
                    new Method(methods).SaveAndSkipMethod(bufferedReader, map, line);
                    continue;
                }
                // checks if it what of global types
                if (HashMapVariable.isLineVariableDeclaration(line)) {  // check if it's a global declaration
                    Variable.compileVariableDeclaration(line, map);
                    continue;
                }
                Scope.compileAssignment(line, map);
                // if it's not a declaration and not a method, assume it's an assign
            }
        }
    }

    /**
     * this is the second run where we go throw the function itself
     * @param path the path where the file is currently
     * @param map a hash map to save all the global arguments
     */
    private static void functionsSearch(String path, HashMapVariable map) throws SjavacException, IOException{
        String line;
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(RegularExpressions.VOID)) {
                    Method method = new Method(methods);
                    method.compileMethodBody(bufferedReader, map, line);
                }
            }
        }
    }


}

