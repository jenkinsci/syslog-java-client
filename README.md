# Syslog Java Client

## Description

Client library written in Java to send messages to a Syslog server.

 * `SyslogMessageSender`: send messages to a Syslog Server. Support implementations
   * `UdpSyslogMessageSender`: [RFC 3164 - The BSD syslog Protocol](http://tools.ietf.org/html/rfc3164) and [RFC 5426 - Transmission of Syslog Messages over UDP](http://tools.ietf.org/html/rfc5426)
   * `TcpSyslogMessageSender`: [RFC 6587 - Transmission of Syslog Messages over TCP](http://tools.ietf.org/html/rfc5426) (including SSL support)
 * `com.cloudbees.syslog.integration.jul.SyslogHandler`: java.util.logging handler to output log messages to a Syslog server.

## Release Notes

See https://github.com/CloudBees-community/syslog-java-client/releases/tag/syslog-java-client-1.1.0

## Using the Syslog Java Client with Maven 

Add the following dependency in your pom.xml:

```xml
<dependency>
    <groupId>com.cloudbees</groupId>
    <artifactId>syslog-java-client</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Sample UDP sender using RFC 3164

```java

// Initialise sender
UdpSyslogMessageSender messageSender = new UdpSyslogMessageSender();
messageSender.setDefaultMessageHostname("myhostname"); // some syslog cloud services may use this field to transmit a secret key
messageSender.setDefaultAppName("myapp");
messageSender.setDefaultFacility(Facility.USER);
messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
messageSender.setSyslogServerHostname("127.0.0.1");
messageSender.setSyslogServerPort(1234);
messageSender.setMessageFormat(MessageFormat.RFC_3164); // optional, default is RFC 3164


// send a Syslog message
messageSender.sendMessage("This is a test message");
```

## Sample UDP sender using RFC 5424

```java

// Initialise sender
SyslogMessageUdpSender messageSender = new SyslogMessageUdpSender();
messageSender.setDefaultMessageHostname("myhostname"); // some syslog cloud services may use this field to transmit a secret key
messageSender.setDefaultAppName("myapp");
messageSender.setDefaultFacility(Facility.USER);
messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
messageSender.setSyslogServerHostname("127.0.0.1");
messageSender.setSyslogServerPort(1234);
messageSender.setMessageFormat(MessageFormat.RFC_5424);

// send a Syslog message
messageSender.sendMessage("This is a test message");
```

## Sample TCP sender using RFC 3164

```java

// Initialise sender
TcpSyslogMessageSender messageSender = new TcpSyslogMessageSender();
messageSender.setDefaultMessageHostname("myhostname"); // some syslog cloud services may use this field to transmit a secret key
messageSender.setDefaultAppName("myapp");
messageSender.setDefaultFacility(Facility.USER);
messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
messageSender.setSyslogServerHostname("127.0.0.1");
messageSender.setSyslogServerPort(1234);
messageSender.setMessageFormat(MessageFormat.RFC_3164); // optional, default is RFC 3164
messageSender.setSsl(false);

// send a Syslog message
messageSender.sendMessage("This is a test message");
```

## Sample TCP over SSL sender using RFC 3164

```java

// Initialise sender
TcpSyslogMessageSender messageSender = new TcpSyslogMessageSender();
messageSender.setDefaultMessageHostname("myhostname"); // some syslog cloud services may use this field to transmit a secret key
messageSender.setDefaultAppName("myapp");
messageSender.setDefaultFacility(Facility.USER);
messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
messageSender.setSyslogServerHostname("127.0.0.1");
messageSender.setSyslogServerPort(1234);
messageSender.setMessageFormat(MessageFormat.RFC_3164); // optional, default is RFC 3164
messageSender.setSsl(true);

// send a Syslog message
messageSender.sendMessage("This is a test message");
```
