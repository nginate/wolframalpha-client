package com.github.nginate.wolframalpha.retrofit.converter;

import com.github.nginate.wolframalpha.model.params.GeoCoordinates;
import retrofit2.Converter;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Locale;

import static java.lang.String.format;

public class GeoCoordsConverter implements Converter<GeoCoordinates, String> {
    @Override
    public String convert(@Nullable GeoCoordinates value) throws IOException {
        if (value == null) {
            return null;
        }
        GeoCoordinates coordinates = GeoCoordinates.class.cast(value);
        return format(Locale.US, "%f,%f", coordinates.getLatitude(), coordinates.getLongitude());
    }
}
