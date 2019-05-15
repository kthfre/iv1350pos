package se.kth.iv1350.registersystem.controller;

/**
 * Exception to be thrown in recoverable situations.
 */

public class GeneralFailureException extends Exception {

    /**
     * Creates and instance of exception.
     *
     * @param msg the error message to be passed to superclass constructor.
     * @param e the exception that was the cause for this exception being thrown.
     */

    public GeneralFailureException(String msg, Exception e) {
        super(msg, e);
    }
}
