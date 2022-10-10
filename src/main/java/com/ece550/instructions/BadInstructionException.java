package  com.ece550.instructions;

public class BadInstructionException extends IllegalArgumentException {

    public static final String MSG_TEMPLATE = "Bad %s type instruction: %s\n%s";
    public static final String ARG_MSG = "Incorrect arg count: expected %d, got %d";

    public BadInstructionException(String message){
        super(message);
    }
}
