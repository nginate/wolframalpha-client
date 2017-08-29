package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.simple.SimpleApi;
import com.github.nginate.wolframalpha.spoken.SpokenResultsApi;
import feign.Feign;
import feign.Logger;
import feign.slf4j.Slf4jLogger;
import lombok.experimental.UtilityClass;

/**
 * Utility configurations to provide client for particular APIs
 */
@UtilityClass
public class ClientFactory {

    /**
     * Build Simple API client using default API url (https://api.wolframalpha.com) and log level (FULL)
     *
     * @return Simple API client
     */
    public static SimpleApi simpleApiClient() {
        return simpleApiClient(Logger.Level.FULL, "https://api.wolframalpha.com");
    }

    /**
     * Build Simple API client using default API url (https://api.wolframalpha.com)
     *
     * @param logLevel logging level to use for internal feign flow
     * @return Simple API client
     */
    public static SimpleApi simpleApiClient(Logger.Level logLevel) {
        return simpleApiClient(logLevel, "https://api.wolframalpha.com");
    }

    /**
     * Build Simple API client
     *
     * @param logLevel logging level to use for internal feign flow
     * @param url      API url
     * @return Simple API client
     */
    public static SimpleApi simpleApiClient(Logger.Level logLevel, String url) {
        return Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(logLevel)
                .target(SimpleApi.class, url);
    }

    /**
     * Build Spoken results API client using default API url (https://api.wolframalpha.com) and log level (FULL)
     *
     * @return Spoken results API client
     */
    public static SpokenResultsApi spokenResultsApi() {
        return spokenResultsApi(Logger.Level.FULL, "https://api.wolframalpha.com");
    }

    /**
     * Build Spoken results API client using default API url (https://api.wolframalpha.com)
     *
     * @param logLevel logging level to use for internal feign flow
     * @return Spoken results API client
     */
    public static SpokenResultsApi spokenResultsApi(Logger.Level logLevel) {
        return spokenResultsApi(logLevel, "https://api.wolframalpha.com");
    }

    /**
     * Build Spoken results API client
     *
     * @param logLevel logging level to use for internal feign flow
     * @param url      API url
     * @return Spoken results API client
     */
    public static SpokenResultsApi spokenResultsApi(Logger.Level logLevel, String url) {
        return Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(logLevel)
                .target(SpokenResultsApi.class, url);
    }
}
