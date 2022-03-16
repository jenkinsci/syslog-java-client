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
package com.cloudbees.syslog;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.cloudbees.syslog.util.CachingReference;
import com.cloudbees.syslog.util.ConcurrentDateFormat;

/**
 * Syslog message as defined in <a href="https://tools.ietf.org/html/rfc5424">RFC 5424 - The Syslog Protocol</a>.
 *
 * Also compatible with <a href="http://tools.ietf.org/html/rfc3164">RFC-3164: The BSD syslog Protocol</a>,
 *
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class SyslogMessage {
    public final static char SP = ' ';
    public final static char NILVALUE = '-';

    private final static int DEFAULT_CONCURRENCY = 50;
    protected final static ConcurrentDateFormat rfc3339DateFormat;
    protected final static ConcurrentDateFormat rfc3164DateFormat;
    private static CachingReference<String> localhostNameReference = new CachingReference<String>(10, TimeUnit.SECONDS) {
        @Override
        protected String newObject() {
            try {
                return InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                return String.valueOf(NILVALUE);
            }
        }
    };

    static {
        int concurrency;
        try {
            concurrency = Integer.parseInt(System.getProperty(SyslogMessage.class.getPackage().getName() + ".concurrency", String.valueOf(DEFAULT_CONCURRENCY)));
        } catch (Exception e) {
            concurrency = DEFAULT_CONCURRENCY;
        }

        rfc3339DateFormat = new ConcurrentDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.US,
                TimeZone.getTimeZone("GMT"),
                concurrency);

        /**
         * According to <a href="http://tools.ietf.org/html/rfc3164#section-4.1.2">RFC31614- 4.1.2 HEADER Part of a syslog Packet</a>,
         * we should use local time and not GMT.
         * <quote>
         *     The TIMESTAMP field is the local time and is in the format of "Mmm dd hh:mm:ss" (without the quote marks)
         * </quote>
         */
        rfc3164DateFormat = new ConcurrentDateFormat(
                "MMM dd HH:mm:ss",
                Locale.US,
                TimeZone.getDefault(),
                concurrency);
    }

    private Facility facility;
    private Severity severity;
    private Long timestamp;
    private String hostname;
    private String appName;
    private String procId;
    private String msgId;
    private Set<SDElement> sdElements;
    /**
     * Use a {@link java.io.CharArrayWriter} instead of a {@link String}  or a {@code char[]} because middlewares like
     * Apache Tomcat use {@code CharArrayWriter} and it's convenient for pooling objects.
     */
    private CharArrayWriter msg;

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public SyslogMessage withFacility(Facility facility) {
        this.facility = facility;
        return this;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public SyslogMessage withSeverity(Severity severity) {
        this.severity = severity;
        return this;
    }

    public Date getTimestamp() {
        return timestamp == null ? null : new Date(timestamp);
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = (timestamp == null ? null : timestamp.getTime());
    }

    public SyslogMessage withTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SyslogMessage withTimestamp(Date timestamp) {
        this.timestamp = (timestamp == null ? null : timestamp.getTime());
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public SyslogMessage withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public SyslogMessage withAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public SyslogMessage withProcId(String procId) {
        this.procId = procId;
        return this;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public SyslogMessage withMsgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public CharArrayWriter getMsg() {
        return msg;
    }

    public void setMsg(CharArrayWriter msg) {
        this.msg = msg;
    }

    public SyslogMessage withMsg(CharArrayWriter msg) {
        this.msg = msg;
        return this;
    }

    public SyslogMessage withMsg(final String msg) {
        return withMsg(new CharArrayWriter() {
            {
                append(msg);
            }
        });
    }
    
    public Set<SDElement> getSDElements() {
        Set<SDElement> ssde = sdElements;
        if (ssde == null) {
            ssde = new HashSet<SDElement>(0);
        }
        return ssde;
    }
    
    public void setSDElements(Set<SDElement> ssde) {
        this.sdElements = ssde;
    }
    
    public SyslogMessage withSDElement(SDElement sde) {
        if (sdElements == null) {
            sdElements = new HashSet<SDElement>();
        }
        sdElements.add(sde);
        return this;
    }

    /**
     * Generates a Syslog message complying to the <a href="http://tools.ietf.org/html/rfc5424">RFC-5424</a> format
     * or to the <a href="http://tools.ietf.org/html/rfc3164">RFC-3164</a> format.
     *
     * @param messageFormat message format, not {@code null}
     */
    public String toSyslogMessage(MessageFormat messageFormat) {
        switch (messageFormat) {
            case RFC_3164:
                return toRfc3164SyslogMessage();
            case RFC_5424:
                return toRfc5424SyslogMessage();
            case RFC_5425:
                return toRfc5425SyslogMessage();
            default:
                throw new IllegalStateException("Unsupported message format '" + messageFormat + "'");
        }
    }

    /**
     * Generates a Syslog message complying to the <a href="http://tools.ietf.org/html/rfc5424">RFC-5424</a> format
     * or to the <a href="http://tools.ietf.org/html/rfc3164">RFC-3164</a> format.
     *
     * @param messageFormat message format
     * @param out                 output {@linkplain Writer}
     */
    public void toSyslogMessage(@NonNull MessageFormat messageFormat, @NonNull Writer out) throws IOException {
        switch (messageFormat) {
            case RFC_3164:
                toRfc3164SyslogMessage(out);
                break;
            case RFC_5424:
                toRfc5424SyslogMessage(out);
                break;
            case RFC_5425:
                toRfc5425SyslogMessage(out);
                break;
            default:
                throw new IllegalStateException("Unsupported message format '" + messageFormat + "'");
        }
    }

    /**
     * Generates an <a href="http://tools.ietf.org/html/rfc5424">RFC-5425</a> message.
     */
    public String toRfc5425SyslogMessage() {

        StringWriter sw = new StringWriter(msg == null ? 32 : msg.size() + 32);
        try {
            toRfc5425SyslogMessage(sw);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return sw.toString();
    }

    /**
     * Generates an <a href="http://tools.ietf.org/html/rfc5425">RFC-5425</a> message.
     */
    public void toRfc5425SyslogMessage(Writer out) throws IOException {

        String rfc5424Message = toRfc5424SyslogMessage();
        int length = rfc5424Message.getBytes(StandardCharsets.UTF_8).length;
        out.write(String.valueOf(length));
        out.write(SP);
        out.write(rfc5424Message);
    }

    /**
     * Generates an <a href="http://tools.ietf.org/html/rfc5424">RFC-5424</a> message.
     */
    public String toRfc5424SyslogMessage() {

        StringWriter sw = new StringWriter(msg == null ? 32 : msg.size() + 32);
        try {
            toRfc5424SyslogMessage(sw);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return sw.toString();
    }

    /**
     * Generates an <a href="http://tools.ietf.org/html/rfc5424">RFC-5424</a> message.
     *
     * The priority is calculated by facility * 8 + severity, see
     * <a href="https://tools.ietf.org/html/rfc5424#section-6.2.1">RFC-5424, Section 6.2.1</a>
     */
    public void toRfc5424SyslogMessage(Writer out) throws IOException {

        int pri = facility.numericalCode() * 8 + severity.numericalCode();

        out.write('<');
        out.write(String.valueOf(pri));
        out.write('>');
        out.write('1'); // version
        out.write(SP);
        out.write(rfc3339DateFormat.format(timestamp == null ? new Date() : new Date(timestamp))); // message time
        out.write(SP);
        out.write(hostname == null ? localhostNameReference.get() : hostname); // emitting server hostname
        out.write(SP);
        writeNillableValue(appName, out); // appname
        out.write(SP);
        writeNillableValue(procId, out); // PID
        out.write(SP);
        writeNillableValue(msgId, out);// Message ID
        out.write(SP);
        writeStructuredDataOrNillableValue(sdElements, out);
        if (msg != null) {
            out.write(SP);
            msg.writeTo(out);
        }
    }

    /**
     * Generates an <a href="http://tools.ietf.org/html/rfc3164">RFC-3164</a> message.
     *
     * @see #toRfc3164SyslogMessage(java.io.Writer)
     */
    public String toRfc3164SyslogMessage() {

        StringWriter sw = new StringWriter(msg == null ? 32 : msg.size() + 32);
        try {
            toRfc3164SyslogMessage(sw);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return sw.toString();
    }

    /**
     * Generates an <a href="http://tools.ietf.org/html/rfc3164">RFC-3164</a> message.
     */
    public void toRfc3164SyslogMessage(Writer out) throws IOException {

        int pri = facility.numericalCode() * 8 + severity.numericalCode();

        out.write('<');
        out.write(Integer.toString(pri));
        out.write('>');
        out.write(rfc3164DateFormat.format(timestamp == null ? new Date() : new Date(timestamp))); // message time
        out.write(SP);
        out.write((hostname == null) ? localhostNameReference.get() : hostname); // emitting server hostname
        out.write(SP);
        writeNillableValue(appName, out); // appname

        if (msg != null) {
            out.write(": ");
            msg.writeTo(out);
        }
    }

    protected void writeNillableValue(@Nullable String value, @NonNull Writer out) throws IOException {
        if (value == null) {
            out.write(NILVALUE);
        } else {
            out.write(value);
        }
    }
    
    protected void writeStructuredDataOrNillableValue(@Nullable Set<SDElement> ssde, @NonNull Writer out) throws IOException {
        if (ssde == null || ssde.isEmpty()) {
            out.write(NILVALUE);
        } else {
            for (SDElement sde : ssde) {
                writeSDElement(sde, out);
            }
        }
    }
    
    protected void writeSDElement(@NonNull SDElement sde, @NonNull Writer out) throws IOException {
        out.write("[");
        out.write(sde.getSdID());
        for (SDParam sdp : sde.getSdParams()) {
            writeSDParam(sdp, out);
        }
        out.write("]");
    }
    
    protected void writeSDParam(@NonNull SDParam sdp, @NonNull Writer out) throws IOException {
        out.write(SP);
        out.write(sdp.getParamName());
        out.write('=');
        out.write('"');
        out.write(getEscapedParamValue(sdp.getParamValue()));
        out.write('"');
    }
    
    protected String getEscapedParamValue(String paramValue) {
        StringBuilder sb = new StringBuilder(paramValue.length());
        
        for (int i = 0; i < paramValue.length(); i++) {
            char c = paramValue.charAt(i);
            switch (c) {
                // Falls through
                case '"':
                case '\\':
                case ']':
                    sb.append('\\');
                    break;
                default:
                    break;
            }
            sb.append(c);
        }
        
        return sb.toString();
    }
}
