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

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class SyslogMessageTest {

    @Test
    public void testRfc5425Format() throws Exception {
        // GIVEN
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

        // WHEN
        String actual = message.toRfc5425SyslogMessage();

        // THEN
        String expected = "81 <14>1 2013-12-05T10:30:05.000Z myserver.example.com my_app - - - a syslog message";
        assertThat(actual, is(expected));
    }

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
    public void testRfc5424FormatWithStructuredData() throws Exception {
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
                .withMsg("a syslog message")
                .withSDElement(new SDElement("exampleSDID@32473", new SDParam("iut", "3"), new SDParam("eventSource", "Application"), new SDParam("eventID", "1011")));

        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2013-12-05T10:30:05.000Z myserver.example.com my_app - - [exampleSDID@32473 iut=\"3\" eventSource=\"Application\" eventID=\"1011\"] a syslog message";

        assertThat(actual, is(expected));
        
        message.withSDElement(new SDElement("examplePriority@32473", new SDParam("class", "high")));
        actual = message.toRfc5424SyslogMessage();
        expected = "<14>1 2013-12-05T10:30:05.000Z myserver.example.com my_app - - [exampleSDID@32473 iut=\"3\" eventSource=\"Application\" eventID=\"1011\"][examplePriority@32473 class=\"high\"] a syslog message";
        
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
