package com.cloudbees.syslog.sender;

import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.SyslogMessage;
import com.cloudbees.syslog.SyslogMessageFormat;
import com.cloudbees.syslog.util.CachingReference;

import javax.annotation.Nonnull;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public abstract class AbstractSyslogMessageSender implements SyslogMessageSender {
    protected final static Charset UTF_8 = Charset.forName("UTF-8");
    protected final Logger logger = Logger.getLogger(getClass().getName());
    // default values
    protected String defaultAppName;
    protected Facility defaultFacility = Facility.USER;
    protected String defaultMessageHostname;
    protected Severity defaultSeverity = Severity.INFORMATIONAL;
    // remote syslog server config
    /**
     * Format of messages accepted by the remote syslog server
     * ({@link com.cloudbees.syslog.SyslogMessageFormat#RFC_3164 RFC_3164} or
     * {@link com.cloudbees.syslog.SyslogMessageFormat#RFC_5424 RFC_5424})
     */
    protected SyslogMessageFormat messageFormat = DEFAULT_SYSLOG_MESSAGE_FORMAT;
    // statistics
    protected final AtomicInteger sendCounter = new AtomicInteger();
    protected final AtomicLong sendDurationInNanosCounter = new AtomicLong();
    protected final AtomicInteger sendErrorCounter = new AtomicInteger();

    /**
     * Send the given text message
     *
     * @param message
     * @throws java.io.IOException
     */
    @Override
    public void sendMessage(CharArrayWriter message) throws IOException {

        SyslogMessage syslogMessage = new SyslogMessage()
                .withAppName(defaultAppName)
                .withFacility(defaultFacility)
                .withHostname(defaultMessageHostname)
                .withSeverity(defaultSeverity)
                .withMsg(message);

        sendMessage(syslogMessage);
    }

    @Override
    public void sendMessage(CharSequence message) throws IOException {
        CharArrayWriter writer = new CharArrayWriter();
        writer.append(message);
        sendMessage(writer);
    }

    /**
     * Send the given {@link com.cloudbees.syslog.SyslogMessage}.
     *
     * @param message the message to send
     * @throws IOException
     */
    public abstract void sendMessage(@Nonnull SyslogMessage message) throws IOException;

    public String getDefaultAppName() {
        return defaultAppName;
    }

    public Facility getDefaultFacility() {
        return defaultFacility;
    }

    public SyslogMessageFormat getMessageFormat() {
        return messageFormat;
    }

    public String getDefaultMessageHostname() {
        return defaultMessageHostname;
    }

    public int getSendCount() {
        return sendCounter.get();
    }

    /**
     * Human readable view of {@link #getSendDurationInNanos()}
     *
     * @return total duration spent sending syslog messages
     */
    public long getSendDurationInMillis() {
        return TimeUnit.MILLISECONDS.convert(getSendDurationInNanos(), TimeUnit.NANOSECONDS);
    }

    /**
     * Human readable view of {@link #getSendDurationInNanos()}
     *
     * @return total duration spent sending syslog messages
     */
    public long getSendDurationInNanos() {
        return sendDurationInNanosCounter.get();
    }

    public int getSendErrorCount() {
        return sendErrorCounter.get();
    }

    public Severity getDefaultSeverity() {
        return defaultSeverity;
    }

    public void setDefaultAppName(String defaultAppName) {
        this.defaultAppName = defaultAppName;
    }

    public void setDefaultMessageHostName(String defaultHostName) {
        this.defaultMessageHostname = defaultHostName;
    }

    public void setDefaultFacility(Facility defaultFacility) {
        this.defaultFacility = defaultFacility;
    }

    public void setMessageFormat(SyslogMessageFormat syslogMessageFormat) {
        this.messageFormat = syslogMessageFormat;
    }

    public void setDefaultSeverity(Severity defaultSeverity) {
        this.defaultSeverity = defaultSeverity;
    }
}
