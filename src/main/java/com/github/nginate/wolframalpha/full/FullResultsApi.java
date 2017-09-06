package com.github.nginate.wolframalpha.full;

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
 */
public interface FullResultsApi {

    default QueryResult getFullResults(String literal, String appId, ResultFormat... formats) {
        return getFullResults(literal, appId, Arrays.asList(formats));
    }

    default QueryResult getFullResultsForAssumptions(String literal, String appId, String... assumptions) {
        return getFullResults(literal, appId, null, Arrays.asList(assumptions));
    }

    default QueryResult getFullResults(String literal, String appId, List<ResultFormat> formats) {
        String serializedFormats = formats.stream()
                .map(Enum::toString).map(String::toLowerCase)
                .reduce((s1, s2) -> format("%s,%s", s1, s2))
                .orElse(null);
        return getFullResults(literal, appId, serializedFormats, Collections.emptyList());
    }

    @RequestLine("GET /v2/query?input={literal}&appid={appid}&format={format}&assumption={assumption}")
    QueryResult getFullResults(@Param("literal") String literal,
                               @Param("appid") String appId,
                               @Param("format") String format,
                               @Param("assumption") List<String> assumptions);
}
