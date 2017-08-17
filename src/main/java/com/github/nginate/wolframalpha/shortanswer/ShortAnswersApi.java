package com.github.nginate.wolframalpha.shortanswer;

import com.github.nginate.wolframalpha.model.Units;
import feign.Param;
import feign.RequestLine;

/**
 * The Short Answers API returns a single plain text result directly from Wolfram|Alpha. In general, this text is taken
 * directly from the Result pod of Wolfram|Alpha output. This API type is designed to deliver brief answers in the most
 * basic format possible. It is implemented in a standard REST protocol using HTTP GET requests.
 * <p>
 * Because this API is designed to return a single result, queries may fail if no sufficiently short result can be
 * found. Although the majority of data available through the Wolfram|Alpha website is also available through this API,
 * certain subjects may be restricted by default.
 */
public interface ShortAnswersApi {

    /**
     * When executed with a valid AppID, this URL will return a short line of text with a computed response to your
     * query. Uses same default timeout as API itself - 5 seconds
     *
     * @param literal URL-encoded input for your query
     * @param appId   The appid parameter tells your query which AppID to use
     * @param units   Use this parameter to manually select what system of units to use for measurements and quantities
     *                (either "metric" or "imperial"). By default, the system will use your location to determine this
     *                setting. Adding "units=metric" to our sample query displays the resulting altitudes in meters
     *                instead of feet
     * @return plain text with response
     */
    @RequestLine("GET /v1/result?i={literal}&appid={appid}&units={units}&timeout={timeout}")
    default String getShortAnswer(@Param("literal") String literal,
                                  @Param("appid") String appId,
                                  @Param("units") Units units) {
        return getShortAnswer(literal, appId, units, 5);
    }

    /**
     * When executed with a valid AppID, this URL will return a short line of text with a computed response to your
     * query
     *
     * @param literal URL-encoded input for your query
     * @param appId   The appid parameter tells your query which AppID to use
     * @param units   Use this parameter to manually select what system of units to use for measurements and quantities
     *                (either "metric" or "imperial"). By default, the system will use your location to determine this
     *                setting. Adding "units=metric" to our sample query displays the resulting altitudes in meters
     *                instead of feet
     * @param timeout This parameter specifies the maximum amount of time (in seconds) allowed to process a query, with
     *                a default value of "5". It is primarily used to optimize response times in applications, although
     *                it may also affect the number and type of results returned by the Simple API.
     * @return plain text with response
     */
    @RequestLine("GET /v1/result?i={literal}&appid={appid}&units={units}&timeout={timeout}")
    String getShortAnswer(@Param("literal") String literal,
                          @Param("appid") String appId,
                          @Param("units") Units units,
                          @Param("timeout") int timeout);
}
