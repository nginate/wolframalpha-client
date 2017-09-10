package com.github.nginate.wolframalpha.feign;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AsyncExpanderTest {
    private AsyncExpander asyncExpander;

    @Before
    public void setUp() throws Exception {
        asyncExpander = new AsyncExpander();
    }

    @Test
    public void nullIsReturnedForNull() throws Exception {
        assertThat(asyncExpander.expand(null)).isNull();
    }

    @Test
    public void falseIsReturnedForNegative() throws Exception {
        assertThat(asyncExpander.expand(-1)).isEqualTo(false);
    }

    @Test
    public void falseIsReturnedForZero() throws Exception {
        assertThat(asyncExpander.expand(0)).isEqualTo(false);
    }

    @Test
    public void trueIsReturnedForPositive() throws Exception {
        assertThat(asyncExpander.expand(1)).isEqualTo(true);
    }
}
