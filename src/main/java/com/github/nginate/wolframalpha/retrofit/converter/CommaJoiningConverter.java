package com.github.nginate.wolframalpha.retrofit.converter;

import retrofit2.Converter;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;

import static java.lang.String.format;

public class CommaJoiningConverter implements Converter<Collection<?>, String> {
    @Override
    public String convert(@Nullable Collection<?> value) throws IOException {
        if (value == null) {
            return null;
        }
        if (value.isEmpty()) {
            return null;
        }
        return value.stream()
                .map(Object::toString)
                .map(String::toLowerCase)
                .reduce((s1, s2) -> format("%s,%s", s1, s2))
                .orElse(null);
    }
}
