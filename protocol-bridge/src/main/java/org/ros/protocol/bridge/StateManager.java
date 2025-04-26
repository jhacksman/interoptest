package org.ros.protocol.bridge;

import java.util.List;

/**
 * Interface for managing state information for ongoing operations.
 */
public interface StateManager {
    /**
     * Register a publisher.
     * 
     * @param nodeName The name of the node
     * @param topicName The name of the topic
     * @param messageType The message type
     */
    void registerPublisher(String nodeName, String topicName, String messageType);
    
    /**
     * Register a subscriber.
     * 
     * @param nodeName The name of the node
     * @param topicName The name of the topic
     * @param messageType The message type
     */
    void registerSubscriber(String nodeName, String topicName, String messageType);
    
    /**
     * Get all publishers for a topic.
     * 
     * @param topicName The name of the topic
     * @return List of publisher node names
     */
    List<String> getPublishers(String topicName);
    
    /**
     * Get all subscribers for a topic.
     * 
     * @param topicName The name of the topic
     * @return List of subscriber node names
     */
    List<String> getSubscribers(String topicName);
}
