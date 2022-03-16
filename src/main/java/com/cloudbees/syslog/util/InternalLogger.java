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

import com.cloudbees.syslog.integration.jul.util.LevelHelper;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Don't use {@link java.util.logging.Logger} as this code can be used as an Handler for java.util.logging and we would then have an infinite loop.
 *
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class InternalLogger {

    private static Level level;

    static {
        try {
            level = LevelHelper.findLevel(System.getProperty("com.cloudbees.syslog.debugLevel"));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public static InternalLogger getLogger(@NonNull String name) {
        return new InternalLogger(name);
    }

    public static InternalLogger getLogger(@NonNull Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Level getLevel() {
        return level;
    }

    public static void setLevel(Level level) {
        InternalLogger.level = level;
    }

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    private final String name;
    /**
     * use java.util.logger to find the logger level if not specified by system property.
     */
    private final Logger julLogger;

    public InternalLogger(String name) {
        this.name = name;
        this.julLogger = Logger.getLogger(name);
    }


    public boolean isLoggable(Level level) {
        if (level == null)
            return false;

        if (this.level == null)
            return julLogger.isLoggable(level);

        return level.intValue() >= this.level.intValue();
    }

    public void finest(@Nullable String msg) {
        log(Level.FINEST, msg);
    }

    public void fine(@Nullable String msg) {
        log(Level.FINE, msg);
    }

    public void finer(@Nullable String msg) {
        log(Level.FINER, msg);
    }

    public void info(@Nullable String msg) {
        log(Level.INFO, msg);
    }

    public void log(@Nullable Level level, @Nullable String msg) {
        log(level, msg, null);
    }

    public void warn(@Nullable String msg) {
        log(Level.WARNING, msg);
    }

    public void warn(@Nullable String msg, @Nullable Throwable t) {
        log(Level.WARNING, msg, t);
    }

    /**
     * synchronize for the {@link java.text.SimpleDateFormat}.
     *
     * @param level
     * @param msg
     * @param t
     */
    public synchronized void log(@Nullable Level level, @Nullable String msg, @Nullable Throwable t) {
        if (!isLoggable(level))
            return;
        System.err.println(df.format(new Date()) + " [" + Thread.currentThread().getName() + "] " + name + " - " + level.getName() + ": " + msg);
        if (t != null)
            t.printStackTrace();

    }

}
