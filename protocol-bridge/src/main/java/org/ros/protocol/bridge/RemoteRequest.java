package org.ros.protocol.bridge;

import java.io.Serializable;

/**
 * Simplified RemoteRequest class to represent RosJavaLite remote method invocations.
 */
public class RemoteRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String className;
    private String methodName;
    private Object[] parameters;
    
    public RemoteRequest(String className, String methodName, Object[] parameters) {
        this.className = className;
        this.methodName = methodName;
        this.parameters = parameters;
    }
    
    public String getClassName() {
        return className;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public Object[] getParameters() {
        return parameters;
    }
}
