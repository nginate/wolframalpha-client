package com.github.nginate.wolframalpha.feign;

import feign.Param;

public class EnumExpander implements Param.Expander {
    @Override
    public String expand(Object value) {
        return value.toString().toLowerCase();
    }
}
