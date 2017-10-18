package com.github.nginate.wolframalpha.feign;

import feign.Param;

import java.util.List;

import static java.lang.String.format;

public class CommaJoinerExpander implements Param.Expander {

    @SuppressWarnings("unchecked")
    @Override
    public String expand(Object value) {
        if (value == null) {
            return null;
        }
        List<Object> list = (List<Object>) value;
        if (list.isEmpty()) {
            return null;
        }
        return list.stream()
                .map(Object::toString)
                .map(String::toLowerCase)
                .reduce((s1, s2) -> format("%s,%s", s1, s2))
                .orElse(null);
    }
}
