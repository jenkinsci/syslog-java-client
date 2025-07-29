package com.cloudbees.syslog.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import org.junit.Test;

public class IoUtilsTest {

	@Test
	public void testCloseQuietlyWithSocket() {
		Socket socket = new Socket();
		IoUtils.closeQuietly(socket);
		assertThat(socket.isClosed(), is(true));

	}

	@Test
	public void testCloseQuietlyWithNullSocket() {
		IoUtils.closeQuietly(null);
		// No exception should be thrown for null socket
	}

	@Test
	public void testCloseQuietlyWithSocketAndWriter() {
		Socket socket = new Socket();
		Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());

		IoUtils.closeQuietly(socket, writer);
		assertThat(socket.isClosed(), is(true));
		// Since we don't mock the writer, we can't directly verify if it's closed
		// However, we know that the method should execute without throwing exceptions

	}

	@Test
	public void testCloseQuietlyWithNullSocketAndNullWriter() {
		IoUtils.closeQuietly(null, null);
		// No exception should be thrown for null socket and null writer
	}

	@Test
	public void testCloseQuietlyWithSocketAndNullWriter() {
		Socket socket = new Socket();
		IoUtils.closeQuietly(socket, null);
		assertThat(socket.isClosed(), is(true));

	}
}