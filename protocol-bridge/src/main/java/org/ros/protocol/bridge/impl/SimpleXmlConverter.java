package org.ros.protocol.bridge.impl;

import org.ros.protocol.bridge.XmlConverter;

/**
 * Simple implementation of XmlConverter as requested by Jon.
 * This is a mock implementation that doesn't actually perform XML conversion.
 */
public class SimpleXmlConverter implements XmlConverter {
    
    @Override
    public String toXML(Object object) {
        return "<object>" + object.toString() + "</object>";
    }
    
    @Override
    public Object fromXML(String xml) {
        return xml;
    }
}
