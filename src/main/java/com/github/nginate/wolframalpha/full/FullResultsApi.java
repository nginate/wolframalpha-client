package com.github.nginate.wolframalpha.full;

import com.github.nginate.wolframalpha.model.Pod;
import com.github.nginate.wolframalpha.model.QueryResult;
import com.github.nginate.wolframalpha.model.ResultFormat;
import com.github.nginate.wolframalpha.model.params.GeoCoordinates;
import com.github.nginate.wolframalpha.model.selection.Selector;
import com.github.nginate.wolframalpha.retrofit.BooleanTimeout;
import com.github.nginate.wolframalpha.retrofit.PayloadAdapter;
import com.github.nginate.wolframalpha.retrofit.interceptor.DocumentedErrorsInterceptor;
import com.github.nginate.wolframalpha.retrofit.interceptor.LoggingInterceptor;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.net.URL;
import java.util.Arrays;
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

    /**
     * API is broken at the time of implementation. Filled a bug but no progress for now. YOu can find details <a
     * href="https://gist.github.com/Kindrat/6af59c6dc3f9ee6bae43a79bc1865e44">here</a>
     * <p>
     * If you really need to use this feature, please, implement some retry mechanism but be aware that it can still be
     * indefinitely reusing same broken Wolfram server.
     *
     * @param asyncPodUri URL for async resource loading
     * @return single POD, loaded by provided url
     */
    @SneakyThrows
    @Deprecated
    default Pod loadPodAsync(String asyncPodUri) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new DocumentedErrorsInterceptor())
                .build();

        HttpUrl httpUrl = HttpUrl.parse(asyncPodUri);
        return new Retrofit.Builder()
                .baseUrl(format("%s://%s/", httpUrl.scheme(), httpUrl.host()))
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(new PayloadAdapter())
                .build()
                .create(AsyncPodApi.class)
                .getAsyncPod(new URL(asyncPodUri).getQuery().substring(3));
    }

    /**
     * Make simple API call for requested {@link ResultFormat response payload types}. All the arguments present in
     * {@link FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float,
     * Float, Float, Float, Float, Float, List, List, List, List, List)} but not listed here are using their defaults.
     *
     * @param input   URL-encoded text specifying the input string. Queries without an input value will fail
     * @param appId   An ID provided by Wolfram Research that identifies the application or organization making the
     *                request
     * @param formats optional array of desired format for individual result pods. Default : Return basic text and image
     *                formats ("plaintext,image").
     * @return {@link QueryResult response} with requested formats or with defaults if formats were not provided
     * @see FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float,
     * Float, Float, Float, Float, Float, List, List, List, List, List)
     */
    default QueryResult getFullResults(String input, String appId, ResultFormat... formats) {
        return getFullResults(input, appId, Arrays.asList(formats), null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null);
    }

    /**
     * Get response with applying provided assumptions. All the arguments present in {@link
     * FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float, Float,
     * Float, Float, Float, Float, List, List, List, List, List)} but not listed here are using their defaults.
     * <p>
     * Wolfram|Alpha makes numerous assumptions when analyzing a query and deciding how to present its results. A simple
     * example is a word that can refer to multiple things, like "pi", which is a well-known mathematical constant but
     * is also the name of a movie. Other classes of assumptions are the meaning of a unit abbreviation like "m", which
     * could be meters or minutes, or the default value of a variable in a formula, or whether 12/13/2001 is a date or a
     * computation. Using the API, you can programmatically invoke these assumption values to alter the output of a
     * query
     *
     * @param input       URL-encoded text specifying the input string. Queries without an input value will fail
     * @param appId       An ID provided by Wolfram Research that identifies the application or organization making the
     *                    request
     * @param assumptions Specifies assumptions, such as the meaning of a word or the value of a formula variable.
     *                    Values for this parameter are given by the input properties of 'value' subelements of
     *                    'assumption' elements in XML results.  ("*C.pi-_*Movie", "DateOrder_**Day.Month.Year--")
     * @return {@link QueryResult response} with applied assumptions
     * @see FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float,
     * Float, Float, Float, Float, Float, List, List, List, List, List)
     */
    default QueryResult getFullResultsForAssumptions(String input, String appId, String... assumptions) {
        return getFullResults(input, appId, null, Arrays.asList(assumptions), null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null);
    }

    /**
     * Make API call with applying one of available pod state simulating mouse clicks on UI. All the arguments present
     * in {@link FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float,
     * Float, Float, Float, Float, Float, List, List, List, List, List)} but not listed here are using their defaults.
     * <p>
     * Many pods on the Wolfram|Alpha website have text buttons in their upper-right corners that substitute the
     * contents of that pod with a modified version (a different state). Clicking any of these buttons will recompute
     * just that one pod to display different information. The API returns information about these pod states and allows
     * you to programmatically invoke them (similar to applying assumptions, described later).
     * <p>
     * A simple example is the query "pi", which returns a pod titled "Decimal approximation" with a button named "More
     * digits". A website user can click this button to replace the pod with a new one showing more digits of pi
     *
     * @param input  URL-encoded text specifying the input string. Queries without an input value will fail
     * @param appId  An ID provided by Wolfram Research that identifies the application or organization making the
     *               request
     * @param states Changing the current state of a pod may also invoke more possible states—in this case, a "Fewer
     *               digits" state is now available, along with an extended "More digits" state. State changes can be
     *               chained together to simulate any sequence of button clicks. You can simulate clicking the "More
     *               digits" button twice as follows:
     *               <p>
     *               <code> http://api.wolframalpha
     *               .com/v2/query?appid=DEMO&input=pi&podstate=DecimalApproximation__More+digits<code/>
     * @return {@link QueryResult response} with applied pod states
     * @see FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float,
     * Float, Float, Float, Float, Float, List, List, List, List, List)
     */
    default QueryResult getFullResultsForPodStates(String input, String appId, String... states) {
        return getFullResults(input, appId, null, null, null, null, null, Arrays.asList(states), null, null, null,
                null, null, null, null, null, null, null, null);
    }

    /**
     * Make an API call specifying location that should be use as client's origin. All the arguments present in {@link
     * FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float, Float,
     * Float, Float, Float, Float, List, List, List, List, List)} but not listed here are using their defaults.
     *
     * @param input    URL-encoded text specifying the input string. Queries without an input value will fail
     * @param appId    An ID provided by Wolfram Research that identifies the application or organization making the
     *                 request
     * @param location Specifies a custom query location based on a string. default : Use caller's IP address for
     *                 location. Only one location parameter may be used at a time. ("Boston, MA", "The North Pole",
     *                 "Beijing")
     * @return location specific {@link QueryResult response}
     * @see FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float,
     * Float, Float, Float, Float, Float, List, List, List, List, List)
     */
    default QueryResult getFullResults(String input, String appId, String location) {
        return getFullResults(input, appId, null, null, location, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null);
    }

    /**
     * Make an API call specifying location that should be use as client's origin. All the arguments present in {@link
     * FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float, Float,
     * Float, Float, Float, Float, List, List, List, List, List)} but not listed here are using their defaults.
     *
     * @param input     URL-encoded text specifying the input string. Queries without an input value will fail
     * @param appId     An ID provided by Wolfram Research that identifies the application or organization making the
     *                  request
     * @param latitude  location latitude
     * @param longitude location longitude
     * @return location specific {@link QueryResult response}
     * @see FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float,
     * Float, Float, Float, Float, Float, List, List, List, List, List)
     */
    default QueryResult getFullResults(String input, String appId, double latitude, double longitude) {
        return getFullResults(input, appId, null, null, null,
                new GeoCoordinates(latitude, longitude), null, null, null, null, null, null, null, null, null, null,
                null, null, null);
    }

    /**
     * Make an API call specifying IP that will be used as as client's location origin. All the arguments present in
     * {@link FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float,
     * Float, Float, Float, Float, Float, List, List, List, List, List)} but not listed here are using their defaults.
     *
     * @param input URL-encoded text specifying the input string. Queries without an input value will fail
     * @param appId An ID provided by Wolfram Research that identifies the application or organization making the
     *              request
     * @param ip    location IP address
     * @return location specific {@link QueryResult response}
     * @see FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float,
     * Float, Float, Float, Float, Float, List, List, List, List, List)
     */
    default QueryResult getFullResultsForIP(String input, String appId, String ip) {
        return getFullResults(input, appId, null, null, null, null, ip, null, null, null, null,
                null, null, null, null, null, null, null, null);
    }

    /**
     * Build request for an API by providing complex conditions of included/excluded pod within special {@link Selector
     * builder}
     *
     * @return {@link Selector request builder} instance
     * @see Selector
     */
    default Selector withCustomSelection() {
        return new Selector(this);
    }

    /**
     * The API allows clients to submit free-form queries similar to the queries one might enter at the Wolfram|Alpha
     * website, and for the computed results to be returned in a variety of formats. It is implemented in a standard
     * REST protocol using HTTP GET requests. Each result is returned as a descriptive XML structure wrapping the
     * requested content format.
     *
     * @param input          URL-encoded text specifying the input string. Queries without an input value will fail
     * @param appId          An ID provided by Wolfram Research that identifies the application or organization making
     *                       the request
     * @param formats        The desired formats for individual result pods. Default : Return basic text and image
     *                       formats ("plaintext,image").
     * @param assumptions    Specifies assumptions, such as the meaning of a word or the value of a formula variable.
     *                       Values for this parameter are given by the input properties of 'value' subelements of
     *                       'assumption' elements in XML results.  ("*C.pi-_*Movie", "DateOrder_**Day.Month.Year--")
     * @param location       Specifies a custom query location based on a string. default : Use caller's IP address for
     *                       location. Only one location parameter may be used at a time. ("Boston, MA", "The North
     *                       Pole", "Beijing")
     * @param latlong        Specifies a custom query location based on a latitude/longitude pair. Default : Use
     *                       caller's IP address for location. ("40.42,-3.71", "40.11, -88.24", "0,0")
     * @param ip             Specifies a custom query location based on an IP address. Only one location parameter may
     *                       be used at a time. IPv4 and IPv6 addresses are supported.
     * @param podStates      Changing the current state of a pod may also invoke more possible states—in this case, a
     *                       "Fewer digits" state is now available, along with an extended "More digits" state. State
     *                       changes can be chained together to simulate any sequence of button clicks. You can simulate
     *                       clicking the "More digits" button twice as follows:
     *                       <p>
     *                       <code> http://api.wolframalpha
     *                       .com/v2/query?appid=DEMO&input=pi&podstate=DecimalApproximation__More
     *                       +digits&podstate =DecimalApproximation__More+digits <code/>
     * @param async          Seconds to wait for result from API. Timedout pod would be retrieved as links for async
     *                       loading
     *                       <p>
     *                       The Wolfram|Alpha website is designed to allow some pods to appear in the user's browser
     *                       before all the pods are ready. For many queries ("weather" is a typical example), you will
     *                       see one to several pods appear quickly, but pods lower down on the screen show up as
     *                       progress bars that have their content spliced in when it becomes available. The
     *                       Wolfram|Alpha server stores certain pod expressions as files before they are formatted, and
     *                       then waits for the client (a web browser, because here we are describing the behavior of
     *                       the website) to request the formatted versions, at which point the formatting stage of the
     *                       computation is performed and the result for each pod is returned as a separate transaction.
     *                       You can get the same behavior in the API using the async parameter.
     *                       <p>
     *                       By default, the API behaves synchronously, meaning that the entire XML document that
     *                       represents the result of a query is returned as a single unit. The caller gets nothing back
     *                       until the entire result is ready. By specifying async=true, you can tell Wolfram|Alpha to
     *                       return an XML document in which some pods are represented as URLs that need to be requested
     *                       in a second step to get their actual XML content. Do not confuse this with image URLs that
     *                       are part of a normal result when the image format type is requested. Although the actual
     *                       data in the images must be requested as a second step, the images themselves are already
     *                       completely generated by the time the original XML result is returned.
     * @param scantimeout    The number of seconds to allow Wolfram|Alpha to compute results in the "scan" stage of
     *                       processing. This parameter effectively limits the number and breadth of subtopics that will
     *                       be included in a result. Default = 3.0
     * @param podtimeout     The number of seconds to allow Wolfram|Alpha to spend in the "format" stage for any one
     *                       pod. This parameter can be used to prevent a single pod from dominating too much processing
     *                       time, or to return only the "quick" information in your result. Default = 4.0
     * @param formattimeout  The number of seconds to allow Wolfram|Alpha to spend in the "format" stage for the entire
     *                       collection of pods. Use this parameter in conjunction with podtimeout to balance between
     *                       returning a few large results and numerous quick results. Default = 8.0
     * @param parsetimeout   The number of seconds to allow Wolfram|Alpha to spend in the "parsing" stage of processing.
     *                       Queries that time out in this phase will return a 'queryresult' element with success=false
     *                       and parsetimedout=true. Very few queries will exceed the default. Default = 5.0
     * @param totaltimeout   The total number of seconds to allow Wolfram|Alpha to spend on a query. Combine with other
     *                       timeout parameters to define a last-resort time limit for queries. Default = 20.0
     * @param includedPodIds Specifies a pod ID to include in the result. By default all pods included. Sample values :
     *                       "Result", "BasicInformation:PeopleData", "DecimalApproximation"
     * @param excludedPodIds Specifies a pod ID to exclude from the result. By default no pods excluded
     * @param podTitles      Specifies a pod title to include in the result. Use * as a wildcard to match zero or more
     *                       characters in pod titles. E.g. "Basic+Information", "Image",
     *                       "Alternative%20representations"
     * @param podIndexes     Specifies the index(es) of the pod(s) to return.
     * @param scanners       Specifies that only pods produced by the given scanner should be returned ("Numeric",
     *                       "Data", "Traveling")
     * @return query result
     * @see QueryResult
     */
    @GET("/v2/query")
    QueryResult getFullResults(@Query("input") String input,
                               @Query("appid") String appId,
                               @Query(value = "format", encoded = true, rawProcessing = true)
                                       List<ResultFormat> formats,
                               @Query("assumption") List<String> assumptions,
                               @Query("location") String location,
                               @Query(value = "latlong", encoded = true) GeoCoordinates latlong,
                               @Query("ip") String ip,
                               @Query("podstate") List<String> podStates,
                               @Query("async") @BooleanTimeout Float async,
                               @Query("scantimeout") Float scantimeout,
                               @Query("podtimeout") Float podtimeout,
                               @Query("formattimeout") Float formattimeout,
                               @Query("parsetimeout") Float parsetimeout,
                               @Query("totaltimeout") Float totaltimeout,
                               @Query("includepodid") List<String> includedPodIds,
                               @Query("excludepodid") List<String> excludedPodIds,
                               @Query("podtitle") List<String> podTitles,
                               @Query("podindex") List<Integer> podIndexes,
                               @Query("scanner") List<String> scanners);


    /**
     * Helper API to wrap dynamic pod retrieval by parsing async url and building special client per each unique URL
     */
    interface AsyncPodApi {
        @GET("api/v2/asyncPod.jsp")
        Pod getAsyncPod(@Query(value = "id", encoded = true) String id);
    }
}
