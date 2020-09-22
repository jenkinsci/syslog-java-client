/*
 * Copyright 2010-2013, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudbees.syslog.sender;

import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.MessageFormat;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.SyslogMessage;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Timestamp;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class TcpSyslogMessageSenderTest {

    // @Ignore
    @Test
    public void send() throws Exception {
        TcpSyslogMessageSender messageSender = new TcpSyslogMessageSender();
        messageSender.setDefaultMessageHostname("mysecretkey");
        messageSender.setDefaultAppName("myapp");
        messageSender.setDefaultFacility(Facility.USER);
        messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
        messageSender.setSyslogServerHostname("logs2.papertrailapp.com");
        messageSender.setSyslogServerPort(46022);
        messageSender.setMessageFormat(MessageFormat.RFC_3164);
        messageSender.setSsl(true);
        messageSender.sendMessage("unit test message over tcp éèà " + getClass() + " - " + new Timestamp(System.currentTimeMillis()));
    }

    @Ignore
    @Test
    public void send2() throws Exception {

        SyslogMessage msg = new SyslogMessage()
                .withAppName("my-app")
                .withFacility(Facility.USER)
                .withHostname("my-hostname")
                .withMsg("my message over tcp éèà " + new Timestamp(System.currentTimeMillis()))
                .withSeverity(Severity.INFORMATIONAL)
                .withTimestamp(System.currentTimeMillis());

        TcpSyslogMessageSender messageSender = new TcpSyslogMessageSender();
        messageSender.setSyslogServerHostname("logs2.papertrailapp.com");
        messageSender.setSyslogServerPort(46022);
        messageSender.setMessageFormat(MessageFormat.RFC_3164);
        messageSender.setSsl(true);

        System.out.println(msg.toSyslogMessage(messageSender.getMessageFormat()));

        messageSender.sendMessage(msg);
    }


    @Ignore
    @Test
    public void sendOverSSL() throws Exception {

        SyslogMessage msg = new SyslogMessage()
                .withAppName("my-app")
                .withFacility(Facility.USER)
                .withHostname("my-hostname")
                .withMsg("my message over tcp ssl éèà " + new Timestamp(System.currentTimeMillis()))
                .withSeverity(Severity.INFORMATIONAL)
                .withTimestamp(System.currentTimeMillis());

        TcpSyslogMessageSender messageSender = new TcpSyslogMessageSender();
        messageSender.setSyslogServerHostname("logs2.papertrailapp.com");
        messageSender.setSyslogServerPort(46022);
        messageSender.setMessageFormat(MessageFormat.RFC_3164);
        messageSender.setSsl(true);

        System.out.println(msg.toSyslogMessage(messageSender.getMessageFormat()));

        messageSender.sendMessage(msg);
    }


    /**
     * https://github.com/CloudBees-community/syslog-java-client/issues/19
     */
    @Test
    public void test_bug19_NullPointerException_In_ToString(){
        TcpSyslogMessageSender tcpSyslogMessageSender = new TcpSyslogMessageSender();
        tcpSyslogMessageSender.toString();
    }
}
