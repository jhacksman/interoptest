package org.ros.protocol.bridge;

/**
 * Main class for the RosJavaLite-rosrust Protocol Bridge.
 * This class coordinates all components to enable interoperability between
 * RosJavaLite and rosrust clients.
 */
public class ProtocolBridge {
    private final XmlRpcServer xmlRpcServer;
    private final ProtocolTranslator protocolTranslator;
    private final JavaClientConnector javaClientConnector;
    private final MessageTypeRegistry messageTypeRegistry;
    private final StateManager stateManager;
    
    public ProtocolBridge(XmlRpcServer xmlRpcServer,
                         ProtocolTranslator protocolTranslator,
                         JavaClientConnector javaClientConnector,
                         MessageTypeRegistry messageTypeRegistry,
                         StateManager stateManager) {
        this.xmlRpcServer = xmlRpcServer;
        this.protocolTranslator = protocolTranslator;
        this.javaClientConnector = javaClientConnector;
        this.messageTypeRegistry = messageTypeRegistry;
        this.stateManager = stateManager;
    }
    
    /**
     * Start the protocol bridge.
     * 
     * @param xmlRpcPort The port for the XML-RPC server
     * @param javaHost The host address of the RosJavaLite master
     * @param javaPort The port of the RosJavaLite master
     * @throws Exception If startup fails
     */
    public void start(int xmlRpcPort, String javaHost, int javaPort) throws Exception {
        javaClientConnector.connect(javaHost, javaPort);
        
        registerHandlers();
        
        xmlRpcServer.start(xmlRpcPort);
    }
    
    /**
     * Stop the protocol bridge.
     */
    public void stop() {
        xmlRpcServer.stop();
        javaClientConnector.disconnect();
    }
    
    private void registerHandlers() {
        xmlRpcServer.registerHandler("registerPublisher", params -> {
            try {
                String xmlRequest = (String) params[0];
                RemoteRequest javaRequest = protocolTranslator.translateXmlRpcToJava(xmlRequest);
                Object javaResponse = javaClientConnector.sendRequest(javaRequest);
                return protocolTranslator.translateJavaToXmlRpc(javaResponse);
            } catch (Exception e) {
                return createErrorResponse(e);
            }
        });
        
        xmlRpcServer.registerHandler("registerSubscriber", params -> {
            try {
                String xmlRequest = (String) params[0];
                RemoteRequest javaRequest = protocolTranslator.translateXmlRpcToJava(xmlRequest);
                Object javaResponse = javaClientConnector.sendRequest(javaRequest);
                return protocolTranslator.translateJavaToXmlRpc(javaResponse);
            } catch (Exception e) {
                return createErrorResponse(e);
            }
        });
        
    }
    
    private String createErrorResponse(Exception e) {
        return "<methodResponse><fault><value><struct>" +
               "<member><name>faultCode</name><value><int>1</int></value></member>" +
               "<member><name>faultString</name><value><string>" + e.getMessage() + "</string></value></member>" +
               "</struct></value></fault></methodResponse>";
    }
}
