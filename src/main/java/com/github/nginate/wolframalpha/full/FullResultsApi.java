package com.github.nginate.wolframalpha.full;

import com.github.nginate.wolframalpha.model.QueryResult;
import feign.Param;
import feign.RequestLine;

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

    @RequestLine("GET /v2/query?input={literal}&appid={appid}")
    QueryResult getFullResults(@Param("literal") String literal,
                               @Param("appid") String appId);
}
