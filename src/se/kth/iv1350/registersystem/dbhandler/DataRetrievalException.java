package se.kth.iv1350.registersystem.dbhandler;

import java.sql.SQLTimeoutException;

/**
 * Exception to be thrown once there is an issue with the remote data retrieval.
 */

public class DataRetrievalException extends RuntimeException {
    private String ip;
    private String port;
    private String reason;
    private String time;
    private String identifier;


    DataRetrievalException(String ip, String port, String reason, String time, String identifier, SQLTimeoutException e) {
        super(time  + "Error connecting to " + ip + ":" + port + "querying for item (" + identifier + "). Reason: " + reason, e);
        this.ip = ip;
        this.port = port;
        this.reason = reason;
        this.time = time;
        this.identifier = identifier;
    }

    /**
     * Gets ip of current exception.
     *
     * @return the ip address of the server.
     */

    public String getIp() {
        return ip;
    }

    /**
     * Gets port of current exception.
     *
     * @return the port of server.
     */

    public String getPort() {
        return port;
    }

    /**
     * Gets reason for failure that lead to the exception.
     *
     * @return the reason for failure-
     */

    public String getReason() {
        return reason;
    }

    /**
     * Gets time once failure occured.
     *
     * @return the time of failure.
     */

    public String getTime() {
        return time;
    }

    /**
     * Gets the identifier of the item that was search once the failure occured.
     *
     * @return the item identifier.
     */

    public String getIdentifier() {
        return identifier;
    }
}
