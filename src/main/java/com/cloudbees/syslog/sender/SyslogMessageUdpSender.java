/*
 * Copyright 2010-2014, CloudBees Inc.
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

import com.cloudbees.syslog.SyslogMessage;
import com.cloudbees.syslog.util.CachingReference;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.logging.Level;

/**
 * Syslog message sender over UDP.
 * <p/>
 * TODO optimize performances recycling the byte arrays. Note: {@link java.io.ByteArrayOutputStream}
 * can be subclassed to access to the underlying {@code byte[]}.
 *
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class SyslogMessageUdpSender extends AbstractSyslogMessageSender {
    /**
     * {@link java.net.InetAddress InetAddress} of the remote Syslog Server.
     * <p/>
     * The {@code InetAddress} is refreshed regularly to handle DNS changes (default {@link #DEFAULT_INET_ADDRESS_TTL_IN_MILLIS})
     * <p/>
     * Default value: {@link #DEFAULT_SYSLOG_HOST}
     */
    protected CachingReference<InetAddress> syslogServerHostnameReference;
    /**
     * Listen port of the remote Syslog server.
     * <p/>
     * Default: {@link #DEFAULT_SYSLOG_PORT}
     */
    protected int syslogServerPort = DEFAULT_SYSLOG_PORT;

    private DatagramSocket datagramSocket;

    public SyslogMessageUdpSender() {
        try {
            setSyslogServerHostname(DEFAULT_SYSLOG_HOST);
            datagramSocket = new DatagramSocket();
        } catch (IOException e) {
            throw new IllegalStateException("Exception initializing datagramSocket", e);
        }
    }

    /**
     * Send the given {@link com.cloudbees.syslog.SyslogMessage} over UDP.
     *
     * @param message the message to send
     * @throws IOException
     */
    @Override
    public void sendMessage(SyslogMessage message) throws IOException {
        sendCounter.incrementAndGet();
        long nanosBefore = System.nanoTime();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter out = new OutputStreamWriter(baos, UTF_8);

            message.toSyslogMessage(messageFormat, out);

            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Send syslog message " + new String(baos.toByteArray(), UTF_8));
            }
            byte[] bytes = baos.toByteArray();
            baos.close();

            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, syslogServerHostnameReference.get(), syslogServerPort);
            datagramSocket.send(packet);
        } catch (IOException e) {
            sendErrorCounter.incrementAndGet();
            throw e;
        } catch (RuntimeException e) {
            sendErrorCounter.incrementAndGet();
            throw e;
        } finally {
            sendDurationInNanosCounter.addAndGet(System.nanoTime() - nanosBefore);
        }
    }


    public void setSyslogServerHostname(final String syslogServerHostname) throws UnknownHostException {
        this.syslogServerHostnameReference = new CachingReference<InetAddress>(DEFAULT_INET_ADDRESS_TTL_IN_NANOS) {
            @Nullable
            @Override
            protected InetAddress newObject() {
                try {
                    return InetAddress.getByName(syslogServerHostname);
                } catch (UnknownHostException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    public void setSyslogServerPort(int syslogServerPort) {
        this.syslogServerPort = syslogServerPort;
    }

    public String getSyslogServerHostname() {
        return syslogServerHostnameReference.get().getHostName();
    }

    public int getSyslogServerPort() {
        return syslogServerPort;
    }
}
