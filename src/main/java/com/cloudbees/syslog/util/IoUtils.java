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
package com.cloudbees.syslog.util;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.net.Socket;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class IoUtils {
    private static final InternalLogger logger = InternalLogger.getLogger(IoUtils.class);

    private IoUtils() {

    }

    public static void closeQuietly(@Nullable Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            logger.warn("Exception while closing the socket "+socket, e);
        }
    }

    /**
     * Note: does not {@link java.io.Writer#flush()} before closing.
     *
     * @param socket
     * @param writer
     */
    public static void closeQuietly(@Nullable Socket socket, @Nullable Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                logger.warn("Exception while closing writer for socket "+socket, e);
            }
        }
        closeQuietly(socket);
    }
}
