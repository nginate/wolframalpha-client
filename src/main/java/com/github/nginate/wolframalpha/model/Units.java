package com.github.nginate.wolframalpha.model;

/**
 * Use this parameter to manually select what system of units to use for measurements and quantities (either "metric" or
 * "imperial"). By default, the system will use your location to determine this setting. Adding "units=metric" to our
 * sample query displays the resulting altitudes in meters instead of feet
 */
public enum Units {
    METRIC, IMPERIAL
}
