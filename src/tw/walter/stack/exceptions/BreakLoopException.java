package tw.walter.stack.exceptions;

/*
 * Walter Stack Language - BreakLoopException
 * Exception used to signify when the "break" keyword is used
 */
public class BreakLoopException extends Exception {
    public BreakLoopException(String errorMessage) {
        super(errorMessage);
    }
}