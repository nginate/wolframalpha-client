package com.github.nginate.wolframalpha.simple;

import com.github.nginate.wolframalpha.model.Layout;
import com.github.nginate.wolframalpha.model.Units;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Immediately get simple images of complete Wolfram|Alpha result pages with the Simple API.
 * <p>
 * Built on the same technology as the Full Results API, the Simple API generates full Wolfram|Alpha output in a
 * universally viewable image format. This API makes it easy to translate free-form linguistic queries into in-depth,
 * formatted results for users with little coding effort. It is implemented in a standard REST protocol using HTTP GET
 * requests.
 * <p>
 * Note: The Simple API does not support disambiguation, drilldown or asynchronous results delivery; it returns only a
 * single, static image. For these advanced features, use the Full Results API.
 * <p>
 * <p> HTTP Status 501
 * <p>
 * This status is returned if a given input value cannot be interpreted by this API. This is commonly caused by input
 * that is blank, misspelled, poorly formatted or otherwise unintelligible. You may occasionally receive this status
 * when requesting information on topics that are restricted or not covered.
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
 * This error is returned when a request does not contain any option for the appid parameter. Double-check that your
 * AppID is typed correctly and that your appid parameter is using the correct syntax.
 *
 * @see <a href="http://products.wolframalpha.com/simple-api/documentation/">documentation reference</a>
 */
public interface SimpleApi {
    /**
     * This call will return an image with informational elements relating to the input.
     *
     * @param literal  URL-encoded input for your query
     * @param appId    The appid parameter tells your query which AppID to use
     * @param fontsize Specify the display size of text elements in points, with a default setting of 14. Oversized text
     *                 (i.e. anything too wide to fit inside your "width" setting) will automatically be hyphenated.
     * @param width    This parameter specifies the desired width (in pixels) for output images, with a default setting
     *                 of "500". In order to display text and images optimally, the actual output size may vary
     *                 slightly. Any text too large to fit will be hyphenated, so it's best to use this in conjunction
     *                 with the fontsize parameter.
     * @param timeout  This parameter specifies the maximum amount of time (in seconds) allowed to process a query, with
     *                 a default value of "5". It is primarily used to optimize response times in applications, although
     *                 it may also affect the number and type of results returned by the Simple API.
     * @return image file
     */
    default Call<byte[]> query(String literal, String appId, int fontsize, int width, int timeout) {
        return query(literal, appId, null, null, null, fontsize, width, null, timeout);
    }

    /**
     * This call will return an image with informational elements relating to the input.
     *
     * @param literal    URL-encoded input for your query
     * @param appId      The appid parameter tells your query which AppID to use
     * @param layout     the layout parameter defines how content is presented. The default setting is divider, which
     *                   specifies a series of pods with horizontal dividers. The other option, labelbar, specifies a
     *                   series of separate content sections with label bar headings
     * @param background This parameter allows you to change the overall background color for visual results. Here is
     *                   the sample result from above, but with a light grey background (#F5F5F5). Colors can be
     *                   expressed as HTML names (e.g. "white"), hexadecimal RGB values (e.g. "00AAFF") or
     *                   comma-separated decimal RGB values (e.g. "0,100,200"). You can also add an alpha channel to RGB
     *                   values (e.g. "0,100,200,200") or specify "transparent" or "clear" for a transparent background.
     *                   The default background color is white.
     * @param foreground Use this parameter to select a foreground color—either "black" (default) or "white"—for text
     *                   elements. The foreground parameter is useful for making text more readable against certain
     *                   background colors. For instance, black text would not show up well against a dark blue
     *                   background, but setting "foreground=white" makes the text easily visible
     * @param fontsize   Specify the display size of text elements in points, with a default setting of 14. Oversized
     *                   text (i.e. anything too wide to fit inside your "width" setting) will automatically be
     *                   hyphenated.
     * @param width      This parameter specifies the desired width (in pixels) for output images, with a default
     *                   setting of "500". In order to display text and images optimally, the actual output size may
     *                   vary slightly. Any text too large to fit will be hyphenated, so it's best to use this in
     *                   conjunction with the fontsize parameter.
     * @param units      Use this parameter to manually select what system of units to use for measurements and
     *                   quantities (either "metric" or "imperial"). By default, the system will use your location to
     *                   determine this setting. Adding "units=metric" to our sample query displays the resulting
     *                   altitudes in meters instead of feet
     * @param timeout    This parameter specifies the maximum amount of time (in seconds) allowed to process a query,
     *                   with a default value of "5". It is primarily used to optimize response times in applications,
     *                   although it may also affect the number and type of results returned by the Simple API.
     * @return image file
     */
    @GET("/v1/simple")
    Call<byte[]> query(@Query("i") String literal,
                       @Query("appid") String appId,
                       @Query("layout") Layout layout,
                       @Query("background") String background,
                       @Query("foreground") String foreground,
                       @Query("fontsize") int fontsize,
                       @Query("width") int width,
                       @Query("units") Units units,
                       @Query("timeout") int timeout);
}
