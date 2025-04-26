package org.ros.protocol.bridge;

import org.junit.Test;
import org.ros.protocol.bridge.impl.SimpleXmlConverter;

import static org.junit.Assert.*;

/**
 * Basic test to demonstrate the protocol bridge framework.
 */
public class ProtocolBridgeTest {
    
    @Test
    public void testXmlConverter() {
        XmlConverter converter = new SimpleXmlConverter();
        
        String xml = converter.toXML("test object");
        assertNotNull(xml);
        assertTrue(xml.contains("test object"));
        
        Object obj = converter.fromXML("<test>data</test>");
        assertNotNull(obj);
        assertEquals("<test>data</test>", obj);
    }
    
    @Test
    public void testRemoteRequest() {
        RemoteRequest request = new RemoteRequest(
            "org.ros.internal.node.server.master.MasterServer",
            "registerPublisher",
            new Object[]{"nodeName", "topicName", "messageType"}
        );
        
        assertEquals("org.ros.internal.node.server.master.MasterServer", request.getClassName());
        assertEquals("registerPublisher", request.getMethodName());
        assertEquals(3, request.getParameters().length);
    }
}
