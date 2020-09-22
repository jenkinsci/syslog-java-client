package com.cloudbees.syslog.sender.proxy;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.Nullable;

import com.cloudbees.syslog.sender.SyslogMessageSender;
import com.cloudbees.syslog.util.CachingReference;

public class ProxyConfig {

	private final int port;
	private final String hostname;
	private final CachingReference<InetAddress> hostnameReference;
	
	public ProxyConfig (final int port, final String hostname) {
		this.port = port;
		this.hostname = hostname;
        this.hostnameReference = new CachingReference<InetAddress>(SyslogMessageSender.DEFAULT_INET_ADDRESS_TTL_IN_NANOS) {
            @Nullable
            @Override
            protected InetAddress newObject() {
                try {
                    return InetAddress.getByName(hostname);
                } catch (UnknownHostException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
	}
	
	public int getPort() {
		return port;
	}
	
	public String getHostname() {
		return hostname;
	}

	public CachingReference<InetAddress> getHostnameReference() {
		return hostnameReference;
	}
	
	
}
