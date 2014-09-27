# Syslog Java Client


## Sample UDP sender using RFC 3614

```java

// Initialise sender
UdpSyslogMessageSender messageSender = new UdpSyslogMessageSender();
messageSender.setDefaultMessageHostName("myhostname"); // some syslog cloud services may use this field to transmit a secret key
messageSender.setDefaultAppName("myapp");
messageSender.setDefaultFacility(Facility.USER);
messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
messageSender.setSyslogServerHostname("127.0.0.1");
messageSender.setSyslogServerPort(1234);
messageSender.setMessageFormat(MessageFormat.RFC_3164); // optional, default is RFC 3164


// send a Syslog message
messageSender.sendMessage("This is a test message");
```

## Sample UDP sender using RFC 3614

```java

// Initialise sender
SyslogMessageUdpSender messageSender = new SyslogMessageUdpSender();
messageSender.setDefaultMessageHostName("myhostname"); // some syslog cloud services may use this field to transmit a secret key
messageSender.setDefaultAppName("myapp");
messageSender.setDefaultFacility(Facility.USER);
messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
messageSender.setSyslogServerHostname("127.0.0.1");
messageSender.setSyslogServerPort(1234);
messageSender.setMessageFormat(MessageFormat.RFC_5424);

// send a Syslog message
messageSender.sendMessage("This is a test message");
```

## Sample TCP sender using RFC 3614

```java

// Initialise sender
TcpSyslogMessageSender messageSender = new TcpSyslogMessageSender();
messageSender.setDefaultMessageHostName("myhostname"); // some syslog cloud services may use this field to transmit a secret key
messageSender.setDefaultAppName("myapp");
messageSender.setDefaultFacility(Facility.USER);
messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
messageSender.setSyslogServerHostname("127.0.0.1");
messageSender.setSyslogServerPort(1234);
messageSender.setMessageFormat(MessageFormat.RFC_3614); // optional, default is RFC 3164
messageSender.setSsl(false);

// send a Syslog message
messageSender.sendMessage("This is a test message");
```

## Sample TCP over SSL sender using RFC 3614

```java

// Initialise sender
TcpSyslogMessageSender messageSender = new TcpSyslogMessageSender();
messageSender.setDefaultMessageHostName("myhostname"); // some syslog cloud services may use this field to transmit a secret key
messageSender.setDefaultAppName("myapp");
messageSender.setDefaultFacility(Facility.USER);
messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
messageSender.setSyslogServerHostname("127.0.0.1");
messageSender.setSyslogServerPort(1234);
messageSender.setMessageFormat(MessageFormat.RFC_3614); // optional, default is RFC 3164
messageSender.setSsl(true);

// send a Syslog message
messageSender.sendMessage("This is a test message");
```