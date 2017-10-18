package com.github.nginate.wolframalpha.model.selection;

import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.model.QueryResult;
import com.github.nginate.wolframalpha.model.ResultFormat;
import com.github.nginate.wolframalpha.model.params.GeoCoordinates;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import java.util.ArrayList;
import java.util.List;

/**
 * Request builder, allowing any combination of non-default values for API request parameters. Any request build process
 * should be terminated by calling {@link Selector#getResults(String, String)}
 *
 * @see FullResultsApi
 */
@RequiredArgsConstructor
public class Selector {
    private final FullResultsApi client;
    private List<ResultFormat> formats;
    private List<String> assumptions;
    private String location;
    private GeoCoordinates coordinates;
    private String ip;
    private List<String> podStates;
    private Float asyncTimeout;
    private Float scanTimeout;
    private Float podTimeout;
    private Float formatTimeout;
    private Float parseTimeout;
    private Float totalTimeout;
    private List<String> includedPods;
    private List<String> excludedPods;
    private List<String> podTitles;
    private List<Integer> indexes;
    private List<String> scanners;

    /**
     * Include result format for API response. Default : Return basic text and image formats ("plaintext,image").
     *
     * @param format format to add
     * @return same selector instance
     */
    @Synchronized
    public Selector withResultFormat(ResultFormat format) {
        if (formats == null) {
            formats = new ArrayList<>();
        }
        formats.add(format);
        return this;
    }

    /**
     * Include result formats for API response. Default : Return basic text and image formats ("plaintext,image").
     *
     * @param formats formats to add
     * @return same selector instance
     */
    @Synchronized
    public Selector withResultFormats(List<ResultFormat> formats) {
        if (this.formats == null) {
            this.formats = new ArrayList<>();
        }
        this.formats.addAll(formats);
        return this;
    }

    /**
     * Include assumption to request. Values for this parameter are given by the input properties of 'value' subelements
     * of 'assumption' elements in XML results.  ("*C.pi-_*Movie", "DateOrder_**Day.Month.Year--")
     *
     * @param assumption assumption value from previous responses
     * @return same selector instance
     */
    @Synchronized
    public Selector withAssumption(String assumption) {
        if (assumptions == null) {
            assumptions = new ArrayList<>();
        }
        assumptions.add(assumption);
        return this;
    }

    /**
     * Use this location name as client location. Useful for location-based requests - e.g. 'what time is it?'
     *
     * @param location some location (e.g. London, LA)
     * @return same selector instance
     */
    public Selector asLocatedIn(String location) {
        this.location = location;
        return this;
    }

    /**
     * Use these coordinates as client location. Useful for location-based requests - e.g. 'what time is it?'
     *
     * @param latitude  lat coordinate
     * @param longitude lon coordinate
     * @return same selector instance
     */
    public Selector asLocatedIn(double latitude, double longitude) {
        this.coordinates = new GeoCoordinates(latitude, longitude);
        return this;
    }

    /**
     * Use this IP as client location. Useful for location-based requests - e.g. 'what time is it?'
     *
     * @param ip IP address
     * @return same selector instance
     */
    public Selector asLocatedAt(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * Specify pod state
     *
     * @param podState pod state to include
     * @return same selector instance
     */
    @Synchronized
    public Selector withPodState(String podState) {
        if (podStates == null) {
            podStates = new ArrayList<>();
        }
        podStates.add(podState);
        return this;
    }

    /**
     * Specify seconds to wait for result from API. Timedout pod would be retrieved as links for async loading
     *
     * @param timeoutSeconds async timeout seconds
     * @return same selector instance
     */
    public Selector usingAsyncTimeout(Float timeoutSeconds) {
        asyncTimeout = timeoutSeconds;
        return this;
    }

    /**
     * Specify the number of seconds to allow Wolfram|Alpha to compute results in the "scan" stage of processing. This
     * parameter effectively limits the number and breadth of subtopics that will be included in a result. Default =
     * 3.0
     *
     * @param scanTimeout scan timeout seconds
     * @return same selector instance
     */
    public Selector usingScanTimeout(Float scanTimeout) {
        this.scanTimeout = scanTimeout;
        return this;
    }

    /**
     * Specify the number of seconds to allow Wolfram|Alpha to spend in the "format" stage for any one pod. This
     * parameter can be used to prevent a single pod from dominating too much processing time, or to return only the
     * "quick" information in your result. Default = 4.0
     *
     * @param podTimeout pod timeout seconds
     * @return same selector instance
     */
    public Selector usingPodTimeout(Float podTimeout) {
        this.podTimeout = podTimeout;
        return this;
    }

    /**
     * Specify the number of seconds to allow Wolfram|Alpha to spend in the "format" stage for the entire collection of
     * pods. Use this parameter in conjunction with podtimeout to balance between returning a few large results and
     * numerous quick results. Default = 8.0
     *
     * @param formatTimeout format timeout seconds
     * @return same selector instance
     */
    public Selector usingFormatTimeout(Float formatTimeout) {
        this.formatTimeout = formatTimeout;
        return this;
    }

    /**
     * Specify the number of seconds to allow Wolfram|Alpha to spend in the "parsing" stage of processing. Queries that
     * time out in this phase will return a 'queryresult' element with success=false and parsetimedout=true. Very few
     * queries will exceed the default. Default = 5.0
     *
     * @param parseTimeout parse timeout seconds
     * @return same selector instance
     */
    public Selector usingParseTimeout(Float parseTimeout) {
        this.parseTimeout = parseTimeout;
        return this;
    }

    /**
     * Specify the total number of seconds to allow Wolfram|Alpha to spend on a query. Combine with other timeout
     * parameters to define a last-resort time limit for queries. Default = 20.0
     *
     * @param totalTimeout total timeout seconds
     * @return same selector instance
     */
    public Selector usingTotalTimeout(Float totalTimeout) {
        this.totalTimeout = totalTimeout;
        return this;
    }

    /**
     * Include pod id to results
     *
     * @param podId pod id to include
     * @return same selector instance
     */
    @Synchronized
    public Selector withPodId(String podId) {
        if (includedPods == null) {
            includedPods = new ArrayList<>();
        }
        includedPods.add(podId);
        if (excludedPods != null) {
            excludedPods.remove(podId);
        }
        return this;
    }

    /**
     * Exclude pod id from results
     *
     * @param podId pod id to exclude
     * @return same selector instance
     */
    @Synchronized
    public Selector withoutPodId(String podId) {
        if (excludedPods == null) {
            excludedPods = new ArrayList<>();
        }
        excludedPods.add(podId);
        if (includedPods != null) {
            includedPods.remove(podId);
        }
        return this;
    }

    /**
     * pecifies a pod title to include in the result. Use * as a wildcard to match zero or more characters in pod
     * titles. E.g. "Basic+Information", "Image", "Alternative%20representations"
     *
     * @param podTitle pod title to include in response
     * @return same selector instance
     */
    @Synchronized
    public Selector withPodTitle(String podTitle) {
        if (podTitles == null) {
            podTitles = new ArrayList<>();
        }
        podTitles.add(podTitle);
        return this;
    }

    /**
     * Specifies the index(es) of the pod(s) to return
     *
     * @param index pod index to include in response
     * @return same selector instance
     */
    @Synchronized
    public Selector withPodIndex(Integer index) {
        if (indexes == null) {
            indexes = new ArrayList<>();
        }
        indexes.add(index);
        return this;
    }

    /**
     * Specifies that only pods produced by the given scanner should be returned ("Numeric", "Data", "Traveling")
     *
     * @param scanner scanner name ("Numeric", "Data", "Traveling")
     * @return same selector instance
     */
    @Synchronized
    public Selector withPodsUsingScanner(String scanner) {
        if (scanners == null) {
            scanners = new ArrayList<>();
        }
        scanners.add(scanner);
        return this;
    }

    /**
     * Terminal operation that is calling API with all provided request parameters.
     *
     * @param input URL-encoded text specifying the input string. Queries without an input value will fail
     * @param appId An ID provided by Wolfram Research that identifies the application or organization making the
     *              request
     * @return {@link QueryResult response}
     * @see FullResultsApi#getFullResults(String, String, List, List, String, GeoCoordinates, String, List, Float,
     * Float, Float, Float, Float, Float, List, List, List, List, List)
     */
    public QueryResult getResults(String input, String appId) {
        return client.getFullResults(input, appId, formats, assumptions, location, coordinates, ip, podStates,
                asyncTimeout, scanTimeout, podTimeout, formatTimeout, parseTimeout, totalTimeout, includedPods,
                excludedPods, podTitles, indexes, scanners);
    }
}
