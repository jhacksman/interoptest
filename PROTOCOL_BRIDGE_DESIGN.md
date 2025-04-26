# RosJavaLite-rosrust Protocol Bridge Design Document

## Executive Summary

This document details the design for a protocol bridge that enables interoperability between RosJavaLite (Java) and rosrust (Rust) ROS client libraries. The bridge acts as a translator between the Java serialization protocol used by RosJavaLite and the XML-RPC protocol used by rosrust, allowing rosrust clients to communicate with a RosJavaLite master node.

## 1. Architecture Overview

### 1.1 High-Level Architecture

```
+---------------------+         +----------------------+         +---------------------+
|                     |         |                      |         |                     |
|  rosrust Clients    |<------->|  Protocol Bridge     |<------->|  RosJavaLite Master |
|  (XML-RPC)          |         |  (Translator)        |         |  (Java Serialization)|
|                     |         |                      |         |                     |
+---------------------+         +----------------------+         +---------------------+
                                        |
                                        v
                                +----------------------+
                                |                      |
                                |  Message Type        |
                                |  Registry            |
                                |                      |
                                +----------------------+
```

The protocol bridge is a standalone service that:
1. Exposes an XML-RPC server endpoint that rosrust clients connect to
2. Translates incoming XML-RPC requests to Java serialization format
3. Forwards the translated requests to the RosJavaLite master
4. Translates the Java serialized responses back to XML-RPC
5. Returns the responses to the rosrust clients

### 1.2 Key Design Principles

- **Minimal Invasiveness**: No changes to the core codebases of either RosJavaLite or rosrust
- **Stateful Translation**: Maintain connection state for complex operations
- **Protocol Fidelity**: Ensure accurate translation of messages between protocols
- **Error Resilience**: Robust error handling and recovery
- **Scalability**: Support multiple concurrent clients

## 2. Key Components and Responsibilities

### 2.1 XML-RPC Server

**Responsibility**: Accept incoming XML-RPC requests from rosrust clients.

- Implements the standard ROS Master API methods
- Listens on a configurable port (default: 11311)
- Parses XML-RPC requests into an internal representation

### 2.2 Protocol Translator

**Responsibility**: Convert between XML-RPC and Java serialization formats.

- Translates XML-RPC method calls to RemoteRequest objects
- Maps ROS API method names to RosJavaLite method signatures
- Converts Java object responses to XML-RPC compatible data structures
- Handles type conversions between Java and XML-RPC data types

### 2.3 Java Client Connector

**Responsibility**: Communicate with the RosJavaLite master.

- Establishes and maintains connection to RosJavaLite master
- Sends translated RemoteRequest objects
- Receives and decodes Java serialized responses
- Handles connection failures and reconnection

### 2.4 Message Type Registry

**Responsibility**: Store and map between message type definitions.

- Maintains a registry of ROS message types
- Provides type information for serialization/deserialization
- Maps between rosrust and RosJavaLite message type representations

### 2.5 State Manager

**Responsibility**: Maintain state information for ongoing operations.

- Tracks active publishers, subscribers, and services
- Maps between rosrust and RosJavaLite node identifiers
- Manages callbacks for publisher updates

## 3. Data Flow

### 3.1 Registration Flow (Example: registerPublisher)

1. rosrust client sends `registerPublisher` XML-RPC request to the bridge
2. XML-RPC Server receives the request and parses parameters
3. Protocol Translator converts XML-RPC call to a RemoteRequest object with:
   - Class name: `org.ros.internal.node.server.master.MasterServer`
   - Method name: `registerPublisher`
   - Parameters: [nodeName, nodeSlaveUri, topicName, topicMessageType]
4. Java Client Connector sends the RemoteRequest to RosJavaLite master
5. RosJavaLite master processes the request and returns a serialized response
6. Java Client Connector receives and deserializes the response
7. Protocol Translator converts Java objects to XML-RPC data structures
8. XML-RPC Server returns the response to the rosrust client
9. State Manager updates its internal registry of publishers

### 3.2 Lookup Flow (Example: lookupService)

1. rosrust client sends `lookupService` XML-RPC request to the bridge
2. Bridge translates and forwards to RosJavaLite master
3. RosJavaLite master returns service URI
4. Bridge translates URI format to rosrust-compatible format
5. Response is returned to rosrust client

### 3.3 Publisher Update Flow

1. RosJavaLite master notifies bridge of new publishers for a topic
2. Bridge translates the notification to XML-RPC format
3. Bridge forwards the notification to registered rosrust subscribers
4. rosrust subscribers update their connection lists

## 4. Protocol Translation Details

### 4.1 Request Translation (XML-RPC to Java Serialization)

XML-RPC Request:
```xml
<methodCall>
  <methodName>registerPublisher</methodName>
  <params>
    <param><value><string>/talker_node</string></value></param>
    <param><value><string>/chatter</string></value></param>
    <param><value><string>std_msgs/String</string></value></param>
    <param><value><string>http://192.168.1.5:1234</string></value></param>
  </params>
</methodCall>
```

Translated to RemoteRequest:
```java
RemoteRequest request = new RemoteRequest(
  "org.ros.internal.node.server.master.MasterServer",
  "registerPublisher",
  new Object[] {
    GraphName.of("/talker_node"),
    new InetSocketAddress("192.168.1.5", 1234),
    GraphName.of("/chatter"),
    "std_msgs/String"
  }
);
```

### 4.2 Response Translation (Java Serialization to XML-RPC)

Java Response (List of InetSocketAddress):
```java
List<InetSocketAddress> subscriberUris = new ArrayList<InetSocketAddress>();
subscriberUris.add(new InetSocketAddress("192.168.1.10", 5678));
```

Translated to XML-RPC:
```xml
<methodResponse>
  <params>
    <param>
      <value>
        <array>
          <data>
            <value><int>1</int></value>
            <value><string>Subscribers found</string></value>
            <value>
              <array>
                <data>
                  <value><string>http://192.168.1.10:5678</string></value>
                </data>
              </array>
            </value>
          </data>
        </array>
      </value>
    </param>
  </params>
</methodResponse>
```

### 4.3 Type Mapping

| XML-RPC Type | Java Type               |
|--------------|-------------------------|
| string       | String                  |
| int          | Integer                 |
| boolean      | Boolean                 |
| double       | Double                  |
| dateTime.iso8601 | Date                |
| base64       | byte[]                  |
| array        | List<Object>            |
| struct       | Map<String, Object>     |
| URI string   | InetSocketAddress       |

## 5. Error Handling and Recovery

### 5.1 Connection Failures

- Implement connection retry mechanism with exponential backoff
- Cache requests during disconnection periods
- Provide notification mechanism for persistent failures

### 5.2 Protocol Errors

- Detailed logging of translation errors
- Graceful degradation for unsupported features
- Clear error messages sent back to clients

### 5.3 State Inconsistency

- Periodic state reconciliation with RosJavaLite master
- Detection and resolution of orphaned registrations
- Heartbeat mechanism to detect node failures

## 6. Implementation Plan

### 6.1 Phase 1: Core Infrastructure (Weeks 1-2)

- Set up XML-RPC server framework
- Implement basic Java serialization client
- Create protocol translation framework
- Implement core ROS API methods (register/unregister)

### 6.2 Phase 2: Complete API Support (Weeks 3-4)

- Implement remaining ROS API methods
- Add message type registry
- Create state management system
- Implement publisher update notification system

### 6.3 Phase 3: Testing and Optimization (Weeks 5-6)

- Develop comprehensive test suite
- Performance optimization
- Error handling improvements
- Documentation and examples

### 6.4 Phase 4: Deployment and Integration (Weeks 7-8)

- Containerization and deployment scripts
- Integration with existing RosJavaLite deployments
- Sample applications demonstrating interoperability
- Final documentation and user guides

## 7. Conclusion

The RosJavaLite-rosrust Protocol Bridge provides a practical solution for enabling interoperability between these two different ROS client library implementations. By translating between Java serialization and XML-RPC protocols, the bridge allows rosrust clients to seamlessly communicate with RosJavaLite master nodes without requiring modifications to either codebase.

This design prioritizes minimal invasiveness while providing robust protocol translation, state management, and error handling capabilities. The implementation plan provides a clear roadmap for building, testing, and deploying the bridge in a production environment.
