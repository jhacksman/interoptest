package org.ros.protocol.bridge;

/**
 * Interface for the XML-RPC server that accepts requests from rosrust clients.
 */
public interface XmlRpcServer {
    /**
     * Start the XML-RPC server on the specified port.
     * 
     * @param port The port to listen on
     */
    void start(int port);
    
    /**
     * Stop the XML-RPC server.
     */
    void stop();
    
    /**
     * Register a handler for XML-RPC method calls.
     * 
     * @param methodName The XML-RPC method name
     * @param handler The handler for this method
     */
    void registerHandler(String methodName, XmlRpcMethodHandler handler);
}
