package opp.ex6.main;

public class SjavacException extends Exception{
    public static final String VAR_ALREADY_EXIST_ERR = "raise error: this variable name is already used in " +
            "this scope: ";
    public static final String INVALID_VAR_NAME_ERR = "raise error: this is not a valid variable name: ";
    public static final String ASSIGN_TO_FINAL_ERR = "raise error: cant assign a value to finale variable";
    public static final String IN_NULL_OR_DIFF_TYPE_ERR = "raise error: there is an inner scope variable " +
            "with different type or a null";
    public static final String OUT_NULL_OR_DIFF_TYPE_ERR = "raise error: there is an outer scope variable " +
            "with different type or a null";
    public static final String INVALID_ASSIGN_ERR = "raise error: not a valid assignment";
    public static final String INVALID_VAR_TYPE_ERR = "raise error: that variable type does not exist: \n";
    public static final String END_COLON_ERR = "raise error: line must end with ;";
    public static final String UNASSIGNED_FINAL_ERR = "raise error: cant declare a final with no assignment";
    public static final String METHOD_NO_RETURN_ERR = "raise error: this method ended with } without doing " +
            "return;";
    public static final String VOID_SPACING_ERR = "raise error: the void does not have space from the name";
    public static final String INVALID_FUNCTION_NAME_ERR = "raise error: the function name was invalid: ";
    public SjavacException(String message) {
        super(message);
    }
}
