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
package com.cloudbees.syslog;

import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class SyslogMessageTest {

    @Test
    public void testRfc5424Format() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(2013, Calendar.DECEMBER, 5, 10, 30, 5);
        cal.set(Calendar.MILLISECOND, 0);

        System.out.println(SyslogMessage.rfc3339DateFormat.format(cal.getTime()));
        System.out.println(cal.getTimeInMillis());


        SyslogMessage message = new SyslogMessage()
                .withTimestamp(cal.getTimeInMillis())
                .withAppName("my_app")
                .withHostname("myserver.example.com")
                .withFacility(Facility.USER)
                .withSeverity(Severity.INFORMATIONAL)
                .withTimestamp(cal.getTimeInMillis())
                .withMsg("a syslog message");

        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2013-12-05T10:30:05.000Z myserver.example.com my_app - - - a syslog message";

        assertThat(actual, is(expected));

    }

    @Test
    public void testRfc3164Format() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());
        cal.set(2013, Calendar.DECEMBER, 5, 10, 30, 5);
        cal.set(Calendar.MILLISECOND, 0);

        System.out.println(SyslogMessage.rfc3339DateFormat.format(cal.getTime()));
        System.out.println(cal.getTimeInMillis());


        SyslogMessage message = new SyslogMessage()
                .withTimestamp(cal.getTimeInMillis())
                .withAppName("my_app")
                .withHostname("myserver.example.com")
                .withFacility(Facility.USER)
                .withSeverity(Severity.INFORMATIONAL)
                .withTimestamp(cal.getTimeInMillis())
                .withMsg("a syslog message");

        String actual = message.toRfc3164SyslogMessage();
        String expected = "<14>Dec 05 10:30:05 myserver.example.com my_app: a syslog message";

        assertThat(actual, is(expected));

    }
}
