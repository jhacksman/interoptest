package org.ros.protocol.bridge;

/**
 * Interface for translating between XML-RPC and Java serialization protocols.
 */
public interface ProtocolTranslator {
    /**
     * Translate an XML-RPC request to a Java RemoteRequest object.
     * 
     * @param xmlRpcRequest The XML-RPC request string
     * @return RemoteRequest object for RosJavaLite
     */
    RemoteRequest translateXmlRpcToJava(String xmlRpcRequest);
    
    /**
     * Translate a Java response object to an XML-RPC response.
     * 
     * @param javaResponse The Java response object
     * @return XML-RPC response string
     */
    String translateJavaToXmlRpc(Object javaResponse);
}
