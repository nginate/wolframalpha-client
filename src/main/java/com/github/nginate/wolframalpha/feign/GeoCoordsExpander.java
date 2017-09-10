package com.github.nginate.wolframalpha.feign;

import com.github.nginate.wolframalpha.model.params.GeoCoordinates;
import feign.Param;

import java.util.Locale;

import static java.lang.String.format;

public class GeoCoordsExpander implements Param.Expander {

    @Override
    public String expand(Object value) {
        if (value == null) {
            return null;
        }
        GeoCoordinates coordinates = GeoCoordinates.class.cast(value);
        return format(Locale.US, "%f,%f", coordinates.getLatitude(), coordinates.getLongitude());
    }
}
