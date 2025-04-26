package org.ros.protocol.bridge;

/**
 * Interface for managing ROS message type definitions.
 */
public interface MessageTypeRegistry {
    /**
     * Register a message type definition.
     * 
     * @param typeName The name of the message type
     * @param typeDefinition The definition of the message type
     */
    void registerType(String typeName, Object typeDefinition);
    
    /**
     * Get the definition for a message type.
     * 
     * @param typeName The name of the message type
     * @return The definition of the message type, or null if not found
     */
    Object getTypeDefinition(String typeName);
    
    /**
     * Check if a message type is registered.
     * 
     * @param typeName The name of the message type
     * @return true if the type is registered, false otherwise
     */
    boolean hasType(String typeName);
}
