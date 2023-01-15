package opp.ex6.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

//test
public class Sjavac {
    private static final HashMap<String, ArrayList<String>> methods = new HashMap<>();

    public static void main(String[] args) {
        try {
            HashMapVariable map = new HashMapVariable();
            globalSearch(args[1], map);
            //map.printMaps();
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
                if (line.equals("") || line.startsWith("//")) { // if line was all comma or an empty line
                    continue;
                }
                if (line.startsWith("void")) {
                    new Method(methods).SaveAndSkipMethod(bufferedReader, map, line);
                    continue;
                }
                if (HashMapVariable.isLineVariableDeclaration(line)) {  // check if it's a global declaration
                    Variable.compileVariableDeclaration(line, true, map);
                    continue;
                }
                Scope.compileAssignment(line, map);
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
                if (line.startsWith(RegularExpressions.VOID)) {
                    Method method = new Method(methods);
                    method.compileMethodBody(bufferedReader, map, line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }








}

