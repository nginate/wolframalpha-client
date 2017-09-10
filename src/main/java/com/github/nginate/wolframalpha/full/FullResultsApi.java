package com.github.nginate.wolframalpha.full;

import com.github.nginate.wolframalpha.feign.AsyncExpander;
import com.github.nginate.wolframalpha.feign.GeoCoordsExpander;
import com.github.nginate.wolframalpha.feign.client.NoNSIRetriableClient;
import com.github.nginate.wolframalpha.model.Pod;
import com.github.nginate.wolframalpha.model.QueryResult;
import com.github.nginate.wolframalpha.model.ResultFormat;
import com.github.nginate.wolframalpha.model.params.GeoCoordinates;
import feign.Feign;
import feign.Logger;
import feign.Param;
import feign.RequestLine;
import feign.jaxb.JAXBDecoder;
import feign.slf4j.Slf4jLogger;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.nginate.wolframalpha.util.SerializationUtil.buildJAXBFactory;
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

    @SneakyThrows
    default Pod loadPodAsync(String asyncPodUri) {
        URL url = new URL(asyncPodUri);

        return Feign.builder()
                .decoder(new JAXBDecoder(buildJAXBFactory()))
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
//                .client(new NoNSIRetriableClient(null, null))
                .target(AsyncPodApi.class, format("%s://%s", url.getProtocol(), url.getHost()))
                .getAsyncPod(url.getQuery().substring(3));
    }

    default QueryResult getFullResults(String input, String appId, ResultFormat... formats) {
        return getFullResults(input, appId, null, Arrays.asList(formats));
    }

    default QueryResult getFullResults(String input, String appId, Double async, ResultFormat... formats) {
        return getFullResults(input, appId, async, Arrays.asList(formats));
    }

    default QueryResult getFullResultsForAssumptions(String input, String appId, String... assumptions) {
        return getFullResults(input, appId, null, Arrays.asList(assumptions), null, null, null, null, null);
    }

    default QueryResult getFullResultsForPodStates(String input, String appId, String... states) {
        return getFullResults(input, appId, null, null, null, null, null, Arrays.asList(states), null);
    }

    default QueryResult getFullResults(String input, String appId, Double async, List<ResultFormat> formats) {
        String serializedFormats = formats.stream()
                .map(Enum::toString).map(String::toLowerCase)
                .reduce((s1, s2) -> format("%s,%s", s1, s2))
                .orElse(null);
        return getFullResults(input, appId, serializedFormats, Collections.emptyList(), null, null, null, null, async);
    }

    default QueryResult getFullResults(String input, String appId, String location) {
        return getFullResults(input, appId, null, Collections.emptyList(), location, null, null, null, null);
    }

    default QueryResult getFullResults(String input, String appId, double latitude, double longitude) {
        return getFullResults(input, appId, null, Collections.emptyList(), null,
                new GeoCoordinates(latitude, longitude), null, null, null);
    }

    default QueryResult getFullResultsForIP(String input, String appId, String ip) {
        return getFullResults(input, appId, null, Collections.emptyList(), null, null, ip, null, null);
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
     * @param podStates   Changing the current state of a pod may also invoke more possible states—in this case, a
     *                    "Fewer digits" state is now available, along with an extended "More digits" state. State
     *                    changes can be chained together to simulate any sequence of button clicks. You can simulate
     *                    clicking the "More digits" button twice as follows:
     *                    <p>
     *                    <code> http://api.wolframalpha
     *                    .com/v2/query?appid=DEMO&input=pi&podstate=DecimalApproximation__More
     *                    +digits&podstate =DecimalApproximation__More+digits <code/>
     * @param async       Seconds to wait for result from API. Timedout pod would be retrieved as links for async
     *                    loading
     *                    <p>
     *                    The Wolfram|Alpha website is designed to allow some pods to appear in the user's browser
     *                    before all the pods are ready. For many queries ("weather" is a typical example), you will see
     *                    one to several pods appear quickly, but pods lower down on the screen show up as progress bars
     *                    that have their content spliced in when it becomes available. The Wolfram|Alpha server stores
     *                    certain pod expressions as files before they are formatted, and then waits for the client (a
     *                    web browser, because here we are describing the behavior of the website) to request the
     *                    formatted versions, at which point the formatting stage of the computation is performed and
     *                    the result for each pod is returned as a separate transaction. You can get the same behavior
     *                    in the API using the async parameter.
     *                    <p>
     *                    By default, the API behaves synchronously, meaning that the entire XML document that
     *                    represents the result of a query is returned as a single unit. The caller gets nothing back
     *                    until the entire result is ready. By specifying async=true, you can tell Wolfram|Alpha to
     *                    return an XML document in which some pods are represented as URLs that need to be requested in
     *                    a second step to get their actual XML content. Do not confuse this with image URLs that are
     *                    part of a normal result when the image format type is requested. Although the actual data in
     *                    the images must be requested as a second step, the images themselves are already completely
     *                    generated by the time the original XML result is returned.
     * @return query result
     * @see QueryResult
     */
    @RequestLine("GET /v2/query?input={input}&appid={appid}&format={format}&assumption={assumption}" +
            "&location={location}&latlong={latlong}&ip={ip}&podstate={podstate}&async={async}")
    QueryResult getFullResults(@Param("input") String input,
                               @Param("appid") String appId,
                               @Param("format") String format,
                               @Param("assumption") List<String> assumptions,
                               @Param("location") String location,
                               @Param(value = "latlong", expander = GeoCoordsExpander.class, encoded = true)
                                       GeoCoordinates latlong,
                               @Param("ip") String ip,
                               @Param(value = "podstate") List<String> podStates,
                               @Param(value = "async", expander = AsyncExpander.class) Double async);


    /**
     * Helper API to wrap dynamic pod retrieval by parsing async url and building special client per each unique URL
     */
    interface AsyncPodApi {
        @RequestLine("GET /api/v2/asyncPod.jsp?id={id}")
        Pod getAsyncPod(@Param(value = "id", encoded = true) String id);
    }
}
