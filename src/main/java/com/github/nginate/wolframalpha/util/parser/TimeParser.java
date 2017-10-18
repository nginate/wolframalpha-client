package com.github.nginate.wolframalpha.util.parser;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Locale.ENGLISH;

@UtilityClass
public class TimeParser {
    private static final DateFormat format = new SimpleDateFormat("HH:mm:ss a zzz  |  EEEE, MMMM d, yyyy", ENGLISH);

    static {
        format.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
    }

    @SneakyThrows
    public static Date parseDateTime(String plainText) {
        return format.parse(plainText);
    }
}
