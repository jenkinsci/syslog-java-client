/*
 * Copyright (c) 2010-2013 the original author or authors
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package com.cloudbees.syslog.integration.jul;

import com.cloudbees.syslog.sender.UdpSyslogMessageSender;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class SyslogHandlerTest {

    @Test
    public void test(){
        Logger logger = Logger.getLogger(getClass().getName());
        logger.setLevel(Level.FINEST);

        UdpSyslogMessageSender messageSender = new UdpSyslogMessageSender();
        SyslogHandler syslogHandler = new SyslogHandler(messageSender, Level.ALL, null);

        messageSender.setSyslogServerHostname("cloudbees1.papertrailapp.com");
        messageSender.setSyslogServerPort(18977);

        syslogHandler.setMessageHostname("mysecretkey");
        syslogHandler.setAppName("SyslogHandlerTest");
        logger.addHandler(syslogHandler);

        logger.fine("hello world 2");
    }
}
