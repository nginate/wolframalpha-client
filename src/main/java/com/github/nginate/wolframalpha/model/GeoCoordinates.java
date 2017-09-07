package com.github.nginate.wolframalpha.model;

import feign.Param;
import lombok.Value;

import static java.lang.String.format;

@Value
public class GeoCoordinates {
    private final double latitude;
    private final double longitude;

}
