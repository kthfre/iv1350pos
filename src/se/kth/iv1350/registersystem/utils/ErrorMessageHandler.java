package se.kth.iv1350.registersystem.utils;

/**
 * Handles error messages for the view in a consistent manner.
 */

public class ErrorMessageHandler {

    /**
     * Creates a default instance.
     *
     */

    public ErrorMessageHandler() {

    }

    /**
     * Prints out the error message to be displayed to the user to the console.
     *
     * @param err the error message to be shown.
     */

    public void printError(String err) {
        System.out.println(err);
    }
}
