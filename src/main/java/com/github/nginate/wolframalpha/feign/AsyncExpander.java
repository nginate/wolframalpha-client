package com.github.nginate.wolframalpha.feign;

import feign.Param;

public class AsyncExpander implements Param.Expander {
    @Override
    public String expand(Object value) {
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
