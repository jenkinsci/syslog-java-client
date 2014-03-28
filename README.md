# Metrics Plugin

This plugin sends Jenkins server logs to a Syslog server.

See also this [plugin's wiki page][wiki]

# Build

To build the plugin locally:

    mvn clean verify

# Release

To release the plugin:

    mvn release:prepare release:perform -B

# Test local instance

To test in a local Jenkins instance

    mvn hpi:run

  [wiki]: http://wiki.jenkins-ci.org/display/JENKINS/Metrics+Plugin