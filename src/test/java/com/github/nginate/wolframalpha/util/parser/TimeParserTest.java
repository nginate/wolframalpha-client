package com.github.nginate.wolframalpha.util.parser;

import org.junit.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeParserTest {
    @Test
    public void testWolframDateParsing() throws Exception {
        String wolframDate = "8:43:14 am UTC  |  Thursday, September 7, 2017";
        Date date = TimeParser.parseDateTime(wolframDate);

        TimeZone aDefault = TimeZone.getDefault();
        int offsetSeconds = (aDefault.getRawOffset() + aDefault.getDSTSavings()) / 1000;
        int offsetMinutes = offsetSeconds / 60;
        int offsetHours = offsetMinutes / 60;

        assertThat(date).isNotNull()
                .hasHourOfDay(8 + offsetHours)
                .hasMinute(43)
                .hasSecond(14)
                .hasYear(2017)
                .hasMonth(9)
                .hasDayOfMonth(7);
    }
}
