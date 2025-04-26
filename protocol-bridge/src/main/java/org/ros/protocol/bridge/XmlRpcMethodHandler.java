package org.ros.protocol.bridge;

/**
 * Interface for handling XML-RPC method calls.
 */
public interface XmlRpcMethodHandler {
    /**
     * Handle an XML-RPC method call.
     * 
     * @param params The parameters for the method call
     * @return The result of the method call
     */
    Object handle(Object[] params);
}
