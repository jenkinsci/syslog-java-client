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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Syslog severity as defined in <a href="https://tools.ietf.org/html/rfc5424">RFC 5424 - The Syslog Protocol</a>.
 *
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public enum Severity {
    /**
     * Emergency: system is unusable, numerical code 0.
     */
    EMERGENCY(0, "EMERGENCY"),
    /**
     * Alert: action must be taken immediately, numerical code 1.
     */
    ALERT(1, "ALERT"),
    /**
     * Critical: critical conditions, numerical code 2.
     */
    CRITICAL(2, "CRITICAL"),
    /**
     * Error: error conditions, numerical code 3.
     */
    ERROR(3, "ERROR"),
    /**
     * Warning: warning conditions, numerical code 4.
     */
    WARNING(4, "WARNING"),
    /**
     * Notice: normal but significant condition, numerical code 5.
     */
    NOTICE(5, "NOTICE"),
    /**
     * Informational: informational messages, numerical code 6.
     */
    INFORMATIONAL(6, "INFORMATIONAL"),
    /**
     * Debug: debug-level messages, numerical code 7.
     */
    DEBUG(7, "DEBUG");

    // mapping
    private final static Map<String, Severity> severityFromLabel = new HashMap<>();
    private final static Map<Integer, Severity> severityFromNumericalCode = new HashMap<>();

    static {
        for (Severity severity : Severity.values()) {
            severityFromLabel.put(severity.label, severity);
            severityFromNumericalCode.put(severity.numericalCode, severity);
        }
    }

    private final int numericalCode;
    @NonNull
    private final String label;

    Severity(int numericalCode, @NonNull String label) {
        this.numericalCode = numericalCode;
        this.label = label;
    }

    /**
     * @param numericalCode Syslog severity numerical code
     * @return Syslog severity, not {@code null}
     * @throws IllegalArgumentException the given numericalCode is not a valid Syslog severity numerical code
     */
    public static Severity fromNumericalCode(int numericalCode) throws IllegalArgumentException {
        Severity severity = severityFromNumericalCode.get(numericalCode);
        if (severity == null) {
            throw new IllegalArgumentException("Invalid severity '" + numericalCode + "'");
        }
        return severity;
    }

    /**
     * @param label Syslog severity textual code. {@code null} or empty returns {@code null}
     * @return Syslog severity, {@code null} if given value is {@code null}
     * @throws IllegalArgumentException the given value is not a valid Syslog severity textual code
     */
    @Nullable
    public static Severity fromLabel(@Nullable String label) throws IllegalArgumentException {
        if (label == null || label.isEmpty())
            return null;

        Severity severity = severityFromLabel.get(label);
        if (severity == null) {
            throw new IllegalArgumentException("Invalid severity '" + label + "'");
        }
        return severity;
    }

    /**
     * Syslog severity numerical code
     */
    public int numericalCode() {
        return numericalCode;
    }

    /**
     * Syslog severity textual code. Not {@code null}.
     */
    @NonNull
    public String label() {
        return label;
    }

    /**
     * Compare on {@link Severity#numericalCode()}
     */
    public static Comparator<Severity> comparator() {
        return Comparator.comparingInt(s -> s.numericalCode);
    }
}

