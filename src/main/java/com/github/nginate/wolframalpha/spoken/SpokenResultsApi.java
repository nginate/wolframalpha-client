package com.github.nginate.wolframalpha.spoken;

import com.github.nginate.wolframalpha.model.Units;
import feign.Param;
import feign.RequestLine;

/**
 * The Spoken Results API returns text results phrased in full sentence form. This API is ideal for applications that
 * use a text-to-speech component to deliver a spoken result to the user. It is implemented in a standard REST protocol
 * using HTTP GET requests.
 * <p>
 * Because this API is designed to return a single result, queries may fail if no sufficiently short result can be
 * found. Although the majority of data available through the Wolfram|Alpha website is also available through this API,
 * certain subjects may be restricted by default.
 * <p>
 * HTTP Status 501
 * <p>
 * This status is returned if a given input value cannot be interpreted by this API. This is commonly caused by input
 * that is misspelled, poorly formatted or otherwise unintelligible. Because this API is designed to return a single
 * result, this message may appear if no sufficiently short result can be found. You may occasionally receive this
 * status when requesting information on topics that are restricted or not covered.
 * <p>
 * HTTP Status 400
 * <p>
 * This status indicates that the API did not find an input parameter while parsing. In most cases, this can be fixed by
 * checking that you have used the correct syntax for including the i parameter.
 * <p>
 * Invalid appid (Error 1)
 * <p>
 * This error is returned when a request contains an invalid option for the appid parameter. Double-check that your
 * AppID is typed correctly and that your appid parameter is using the correct syntax.
 * <p>
 * Appid missing (Error 2)
 * <p>
 * This error is returned when a request does not contain any option for the appid parameter. Double-check that you have
 * entered an AppID and that your appid parameter is using the correct syntax.
 *
 * @see <a href="http://products.wolframalpha.com/spoken-results-api/documentation/">documentation reference</>
 */
public interface SpokenResultsApi {

    /**
     * When executed with a valid AppID, this URL will return a single line of conversational text with a computed
     * response to your query. Uses same default timeout as API itself - 5 seconds
     *
     * @param literal URL-encoded input for your query
     * @param appId   The appid parameter tells your query which AppID to use
     * @param units   Use this parameter to manually select what system of units to use for measurements and quantities
     *                (either "metric" or "imperial"). By default, the system will use your location to determine this
     *                setting. Adding "units=metric" to our sample query displays the resulting altitudes in meters
     *                instead of feet
     * @return text results phrased in full sentence form
     */
    @RequestLine("GET /v1/spoken?i={literal}&appid={appid}&units={units}")
    default String getSpokenResults(@Param("literal") String literal,
                                    @Param("appid") String appId,
                                    @Param("units") Units units) {
        return getSpokenResults(literal, appId, units, 5);
    }

    /**
     * When executed with a valid AppID, this URL will return a single line of conversational text with a computed
     * response to your query
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
     * @return text results phrased in full sentence form
     */
    @RequestLine("GET /v1/spoken?i={literal}&appid={appid}&units={units}&timeout={timeout}")
    String getSpokenResults(@Param("literal") String literal,
                            @Param("appid") String appId,
                            @Param("units") Units units,
                            @Param("timeout") int timeout);
}
