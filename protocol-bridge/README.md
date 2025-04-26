# RosJavaLite-rosrust Protocol Bridge

This is a framework implementation of the protocol bridge that enables interoperability between RosJavaLite (Java) and rosrust (Rust) ROS client libraries.

## Overview

The protocol bridge acts as a translator between the Java serialization protocol used by RosJavaLite and the XML-RPC protocol used by rosrust. This allows rosrust clients to communicate with a RosJavaLite master node without requiring modifications to either codebase.

## Components

1. **XmlRpcServer**: Accepts incoming XML-RPC requests from rosrust clients
2. **ProtocolTranslator**: Converts between XML-RPC and Java serialization formats
3. **JavaClientConnector**: Communicates with the RosJavaLite master
4. **MessageTypeRegistry**: Manages ROS message type definitions
5. **StateManager**: Maintains state information for ongoing operations

## XML Processing Interface

As requested, the framework includes simple XML processing interfaces:
- `String toXML(Object object)`: Convert Java objects to XML strings
- `Object fromXML(String xml)`: Convert XML strings to Java objects

The implementation details of these conversions are intentionally left as mock implementations to minimize dependencies on third-party XML processing packages.

## Building

```bash
mvn clean install
```

## Usage

The `ProtocolBridge` class is the main entry point for the bridge. It coordinates all components and provides methods to start and stop the bridge.

```java
ProtocolBridge bridge = new ProtocolBridge(
    xmlRpcServer,
    protocolTranslator,
    javaClientConnector,
    messageTypeRegistry,
    stateManager
);

bridge.start(11311, "localhost", 8090);
```

## Note

This is a framework implementation that provides the structure for the protocol bridge. The actual XML conversion logic and detailed protocol translation would need to be implemented based on specific requirements.
