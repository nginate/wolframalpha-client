package com.github.nginate.wolframalpha.feign;

import com.github.nginate.wolframalpha.model.params.GeoCoordinates;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GeoCoordsExpanderTest {
    private GeoCoordsExpander expander;

    @Before
    public void setUp() throws Exception {
        expander = new GeoCoordsExpander();
    }

    @Test
    public void testNullConversion() throws Exception {
        assertThat(expander.expand(null)).isNull();
    }

    @Test
    public void testCoordinatesConversion() throws Exception {
        double lat = 10.096456;
        double lon = -50.346436;

        String string = expander.expand(new GeoCoordinates(lat, lon));
        assertThat(string).isNotEmpty().contains(Double.toString(lat)).contains(Double.toString(lon));

        String[] parts = string.split(",");
        assertThat(Double.parseDouble(parts[0])).isEqualTo(lat);
        assertThat(Double.parseDouble(parts[1])).isEqualTo(lon);
    }
}
