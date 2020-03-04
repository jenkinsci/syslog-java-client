package com.cloudbees.syslog.util;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ConcurrentRfc3164DateFormatTest {

    @Test
    public void test_format_date_with_single_digit_day_of_month(){
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(timeZone);
        cal.set(2013, Calendar.AUGUST, 7, 10, 30, 5);
        cal.set(Calendar.MILLISECOND, 0);

        Date singleDigitDayOfMonthDate = cal.getTime();

        ConcurrentRfc3164DateFormat rfc3164DateFormat = new ConcurrentRfc3164DateFormat(Locale.US, timeZone, 50);

        String actual = rfc3164DateFormat.format(singleDigitDayOfMonthDate);

        Assert.assertThat(actual, Matchers.is("Aug  7 10:30:05"));
    }

    @Test
    public void test_format_date_with_double_digit_day_of_month(){
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(timeZone);
        cal.set(2013, Calendar.AUGUST, 17, 10, 30, 5);
        cal.set(Calendar.MILLISECOND, 0);

        Date singleDigitDayOfMonthDate = cal.getTime();

        ConcurrentRfc3164DateFormat rfc3164DateFormat = new ConcurrentRfc3164DateFormat(Locale.US, timeZone, 50);

        String actual = rfc3164DateFormat.format(singleDigitDayOfMonthDate);

        Assert.assertThat(actual, Matchers.is("Aug 17 10:30:05"));
    }
}
