package com.github.nginate.wolframalpha.full;

import com.github.nginate.wolframalpha.model.GeoCoordinates;
import com.github.nginate.wolframalpha.feign.GeoCoordsExpander;
import com.github.nginate.wolframalpha.model.QueryResult;
import com.github.nginate.wolframalpha.model.ResultFormat;
import feign.Param;
import feign.RequestLine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

/**
 * The API allows clients to submit free-form queries similar to the queries one might enter at the Wolfram|Alpha
 * website, and for the computed results to be returned in a variety of formats. It is implemented in a standard REST
 * protocol using HTTP GET requests. Each result is returned as a descriptive XML or JSON structure wrapping the
 * requested content format.
 * <p>
 * Although the majority of data available through the Wolfram|Alpha website is also available through this API, certain
 * subjects may be restricted by default.
 * <p>
 * You can include multiple location parameters in your query, and they will be evaluated in order of precedence
 * (location → latlong → ip) until a valid location is found. In this way, you can use extra location parameters as
 * "backups" in case the initial specification fails. For instance, if you were executing a "weather" query using a
 * location string, you might also want to include a default latitude/longitude location in case the given string is
 * invalid.
 */
public interface FullResultsApi {

    default QueryResult getFullResults(String input, String appId, ResultFormat... formats) {
        return getFullResults(input, appId, Arrays.asList(formats));
    }

    default QueryResult getFullResultsForAssumptions(String input, String appId, String... assumptions) {
        return getFullResults(input, appId, null, Arrays.asList(assumptions), null, null, null);
    }

    default QueryResult getFullResults(String input, String appId, List<ResultFormat> formats) {
        String serializedFormats = formats.stream()
                .map(Enum::toString).map(String::toLowerCase)
                .reduce((s1, s2) -> format("%s,%s", s1, s2))
                .orElse(null);
        return getFullResults(input, appId, serializedFormats, Collections.emptyList(), null, null, null);
    }

    default QueryResult getFullResults(String input, String appId, String location) {
        return getFullResults(input, appId, null, Collections.emptyList(), location, null, null);
    }

    default QueryResult getFullResults(String input, String appId, GeoCoordinates location) {
        return getFullResults(input, appId, null, Collections.emptyList(), null, location, null);
    }

    default QueryResult getFullResults(String input, String appId, double latitude, double longitude) {
        return getFullResults(input, appId, null, Collections.emptyList(), null,
                new GeoCoordinates(latitude, longitude), null);
    }

    default QueryResult getFullResultsForIP(String input, String appId, String ip) {
        return getFullResults(input, appId, null, Collections.emptyList(), null, null, ip);
    }

    /**
     * The API allows clients to submit free-form queries similar to the queries one might enter at the Wolfram|Alpha
     * website, and for the computed results to be returned in a variety of formats. It is implemented in a standard
     * REST protocol using HTTP GET requests. Each result is returned as a descriptive XML structure wrapping the
     * requested content format.
     *
     * @param input       URL-encoded text specifying the input string. Queries without an input value will fail
     * @param appId       An ID provided by Wolfram Research that identifies the application or organization making the
     *                    request
     * @param format      The desired format for individual result pods. Default : Return basic text and image formats
     *                    ("plaintext,image").
     * @param assumptions Specifies assumptions, such as the meaning of a word or the value of a formula variable.
     *                    Values for this parameter are given by the input properties of <value> subelements of
     *                    <assumption> elements in XML results.  ("*C.pi-_*Movie", "DateOrder_**Day.Month.Year--")
     * @param location    Specifies a custom query location based on a string. default : Use caller's IP address for
     *                    location. Only one location parameter may be used at a time. ("Boston, MA", "The North Pole",
     *                    "Beijing")
     * @param latlong     Specifies a custom query location based on a latitude/longitude pair. Default : Use caller's
     *                    IP address for location. ("40.42,-3.71", "40.11, -88.24", "0,0")
     * @param ip          Specifies a custom query location based on an IP address. Only one location parameter may be
     *                    used at a time. IPv4 and IPv6 addresses are supported.
     * @return query result
     * @see QueryResult
     */
    @RequestLine("GET /v2/query?input={input}&appid={appid}&format={format}&assumption={assumption}" +
            "&location={location}&latlong={latlong}&ip={ip}")
    QueryResult getFullResults(@Param("input") String input,
                               @Param("appid") String appId,
                               @Param("format") String format,
                               @Param("assumption") List<String> assumptions,
                               @Param("location") String location,
                               @Param(value = "latlong", expander = GeoCoordsExpander.class, encoded = true)
                                       GeoCoordinates latlong,
                               @Param("ip") String ip);
}
