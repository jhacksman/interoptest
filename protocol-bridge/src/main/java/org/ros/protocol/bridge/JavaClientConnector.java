package org.ros.protocol.bridge;

import java.io.IOException;

/**
 * Interface for connecting to and communicating with the RosJavaLite master.
 */
public interface JavaClientConnector {
    /**
     * Connect to the RosJavaLite master.
     * 
     * @param host The host address
     * @param port The port number
     * @throws IOException If connection fails
     */
    void connect(String host, int port) throws IOException;
    
    /**
     * Send a RemoteRequest to the RosJavaLite master.
     * 
     * @param request The request to send
     * @return The response from the master
     * @throws IOException If communication fails
     */
    Object sendRequest(RemoteRequest request) throws IOException;
    
    /**
     * Disconnect from the RosJavaLite master.
     */
    void disconnect();
}
