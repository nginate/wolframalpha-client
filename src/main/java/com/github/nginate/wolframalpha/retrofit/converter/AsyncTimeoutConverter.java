package com.github.nginate.wolframalpha.retrofit.converter;

import retrofit2.Converter;

import javax.annotation.Nullable;
import java.io.IOException;

public class AsyncTimeoutConverter implements Converter<Float, String> {

    @Override
    public String convert(@Nullable Float value) throws IOException {
        if (value == null) {
            return null;
        }
        Float seconds = Float.class.cast(value);
        if (seconds > 0) {
            return Float.toString(seconds);
        } else {
            return Boolean.FALSE.toString();
        }
    }
}
