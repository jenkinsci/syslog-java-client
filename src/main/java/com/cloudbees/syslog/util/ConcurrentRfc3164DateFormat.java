package com.cloudbees.syslog.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * https://tools.ietf.org/html/rfc3164#section-4.1.2
 * <pre>
 * The TIMESTAMP field is the local time and is in the format of "Mmm dd
 *    hh:mm:ss" (without the quote marks) where:
 *
 *          Mmm is the English language abbreviation for the month of the
 *          year with the first character in uppercase and the other two
 *          characters in lowercase.  The following are the only acceptable
 *          values:
 *
 *          Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
 *
 *          dd is the day of the month.  If the day of the month is less
 *          than 10, then it MUST be represented as a space and then the
 *          number.  For example, the 7th day of August would be
 *          represented as "Aug  7", with two spaces between the "g" and
 *          the "7".
 *
 *          hh:mm:ss is the local time.  The hour (hh) is represented in a
 *          24-hour format.  Valid entries are between 00 and 23,
 *          inclusive.  The minute (mm) and second (ss) entries are between
 *          00 and 59 inclusive.
 *  </pre>
 */
public class ConcurrentRfc3164DateFormat {
    private final BlockingQueue<SimpleDateFormat> monthDateFormats;
    private final BlockingQueue<SimpleDateFormat> timeDateFormats;
    private final Locale locale;
    private final TimeZone timeZone;

    /**
     * @param locale       the locale whose date pattern symbols should be used
     * @param timeZone     the timezone used by the underlying calendar
     * @param maxCacheSize
     * @throws NullPointerException     if the given pattern or locale is null
     * @throws IllegalArgumentException if the given pattern is invalid
     */
    public ConcurrentRfc3164DateFormat(Locale locale, TimeZone timeZone, int maxCacheSize) {
        this.monthDateFormats = new LinkedBlockingDeque<>(maxCacheSize);
        this.timeDateFormats = new LinkedBlockingDeque<>(maxCacheSize);
        this.locale = locale;
        this.timeZone = timeZone;
    }

    /**
     * Formats a Date into a date/time string.
     *
     * @param date the time value to be formatted into a time string.
     * @return the formatted time string.
     */
    @Nonnull
    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_BAD_PRACTICE")
    public String format(@Nonnull Date date) {


        // MONTH

        String month;


        // DAY OF MONTH
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String dayofMonthPadding = dayOfMonth < 10 ? " " : "";

        // TIME
        SimpleDateFormat timeDateFormat = timeDateFormats.poll();
        if (timeDateFormat == null) {
            timeDateFormat = new SimpleDateFormat("HH:mm:ss", locale);
            timeDateFormat.setTimeZone(timeZone);
        }
        String time;
        try {
            time = timeDateFormat.format(date);
        } finally {
            timeDateFormats.offer(timeDateFormat);
        }

        String result = month + ' ' + dayofMonthPadding + dayOfMonth + ' ' + time;

        return result;
    }

    @Override
    public String toString() {
        return "ConcurrentRfc3164DateFormat";
    }
}
