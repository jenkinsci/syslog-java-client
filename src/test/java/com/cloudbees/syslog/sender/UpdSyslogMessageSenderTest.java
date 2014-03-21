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
import com.cloudbees.syslog.Severity;
import org.junit.Test;

import java.sql.Timestamp;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class UpdSyslogMessageSenderTest {

    // @Ignore
    @Test
    public void send() throws Exception {
        UdpSyslogMessageSender messageSender = new UdpSyslogMessageSender();
        messageSender.setDefaultMessageHostname("mysecretkey");
        messageSender.setDefaultAppName("myapp");
        messageSender.setDefaultFacility(Facility.USER);
        messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
        // messageSender.setSyslogServerHostname("cloudbees1.papertrailapp.com");
        messageSender.setSyslogServerHostname("127.0.0.1");
        messageSender.setSyslogServerPort(18977);
        messageSender.sendMessage("unit test message éèà " + getClass() + " - " + new Timestamp(System.currentTimeMillis()));
    }


}
