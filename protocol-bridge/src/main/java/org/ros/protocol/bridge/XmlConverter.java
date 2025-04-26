package org.ros.protocol.bridge;

/**
 * Interface for XML conversion operations as requested by Jon.
 * This provides a simple abstraction for converting between Java objects and XML strings.
 */
public interface XmlConverter {
    /**
     * Convert a Java object to an XML string representation.
     * 
     * @param object The Java object to convert
     * @return XML string representation of the object
     */
    String toXML(Object object);
    
    /**
     * Convert an XML string to a Java object.
     * 
     * @param xml The XML string to convert
     * @return Java object representation of the XML
     */
    Object fromXML(String xml);
}
