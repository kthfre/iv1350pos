package se.kth.iv1350.registersystem.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Log handler that handles the task of printing errors for developers for debugging purposes.
 */

public class LogHandler {

    /**
     * Creates a default instance.
     */

    public LogHandler() {

    }

    /**
     * Prints the debug message from the exception to the log along with current time.
     *
     * @param msg the error message to be printed.
     * @param e the exception that is being logged.
     */

    public void consoleLog(String msg, Exception e){
        System.out.println();
        System.out.println("DEBUG LOG - " + currentTime());
        System.out.println(msg);

        if (e.getCause() != null) {
            System.out.println(e.getCause());
        }

        System.out.println("END OF DEBUG LOG.");
        System.out.println();
    }

    private String currentTime() {
        DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.UK).withZone(ZoneId.systemDefault());
        return format.format(Instant.now());
    }
}
