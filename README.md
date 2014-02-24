# Syslog Java Client

```java

// Initialise sender
SyslogMessageUdpSender messageSender = new SyslogMessageUdpSender();
messageSender.setDefaultMessageHostName("myhostname"); // some syslog cloud services may use this field to transmit a secret key
messageSender.setDefaultAppName("myapp");
messageSender.setDefaultFacility(Facility.USER);
messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
messageSender.setSyslogServerHostname("127.0.0.1");
messageSender.setSyslogServerPort(1234);

// send a Syslog message
messageSender.sendMessage("This is a test message");
```