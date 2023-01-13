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
                //TODO: check if this line is an assignment (without declaration) and call the function
                System.out.println("raise error 10");
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
            System.out.println("raise error 3 ");
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
            System.out.println("raise error 4");
        }
    }

    private static void functionsSearch(String path, HashMapVariable map) {  // throws InvalidValue / WrongSyntax
        String line;
        try (FileReader fileReader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.equals("")) { // if line was all comma or an empty line
                    continue;
                }
                if (line.startsWith(VOID)) {
                    compileMethod(bufferedReader, map);
                }
            }
        } catch (IOException e) {
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

    /**
     *
     * @param line
     * @param map
     */
    private static void compileAssignment(String line, HashMapVariable map) { // throws InvalidValue / WrongSyntax
        //TODO: compile assignment, check if variable exist in current scope, use Variable.setValue function
        //TODO: handle an uninitialized global or outer scope variable as a new variable in current scope
    }


    /**
     *
     * @param bufferedReader
     * @throws IOException
     */
    private static void skipMethod(BufferedReader bufferedReader) throws IOException {
        int countOpenCurlyBrackets = 1;
        int countCloseCurlyBrackets = 0;
        String line;
        while (countOpenCurlyBrackets != countCloseCurlyBrackets) {
            line = bufferedReader.readLine();
            if (line.contains("}")) {
                countCloseCurlyBrackets += 1;
            } else if (line.startsWith(IF) || line.startsWith(WHILE)) {
                countOpenCurlyBrackets += 1;
            }
            //TODO: write a function that skips a method - from void....{    to the last return;}
        }

//    private static String removeSpacesAndComma(String line) {
//        //TODO: write a function to remove unnecessary spaces from the beginning of line and commas
//        line = line.trim()
//        return line.trim();
//    }
    }
}
