package com.github.nginate.wolframalpha;

import com.github.nginate.wolframalpha.full.FullResultsApi;
import com.github.nginate.wolframalpha.shortanswer.ShortAnswersApi;
import com.github.nginate.wolframalpha.simple.SimpleApi;
import com.github.nginate.wolframalpha.spoken.SpokenResultsApi;
import feign.Feign;
import feign.Logger;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.slf4j.Slf4jLogger;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

/**
 * Utility configurations to provide client for particular APIs
 */
@UtilityClass
public class ClientFactory {

    private static final String url = "https://api.wolframalpha.com";

    /**
     * Build Simple API client using default API url (https://api.wolframalpha.com) and log level (FULL)
     *
     * @return Simple API client
     */
    public static SimpleApi simpleApiClient() {
        return simpleApiClient(Logger.Level.FULL, url);
    }

    /**
     * Build Simple API client using default API url (https://api.wolframalpha.com)
     *
     * @param logLevel logging level to use for internal feign flow
     * @return Simple API client
     */
    public static SimpleApi simpleApiClient(Logger.Level logLevel) {
        return simpleApiClient(logLevel, url);
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
        return spokenResultsApi(Logger.Level.FULL);
    }

    /**
     * Build Spoken results API client using default API url (https://api.wolframalpha.com)
     *
     * @param logLevel logging level to use for internal feign flow
     * @return Spoken results API client
     */
    public static SpokenResultsApi spokenResultsApi(Logger.Level logLevel) {
        return spokenResultsApi(logLevel, url);
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

    /**
     * Build Short answers API client
     *
     * @return Short answers API client
     */
    public static ShortAnswersApi shortAnswersApi() {
        return shortAnswersApi(Logger.Level.FULL, url);
    }

    /**
     * Build Short answers API client
     *
     * @param logLevel logging level to use for internal feign flow
     * @return Short answers API client
     */
    public static ShortAnswersApi shortAnswersApi(Logger.Level logLevel) {
        return shortAnswersApi(logLevel, url);
    }

    /**
     * Build Short answers API client
     *
     * @param logLevel logging level to use for internal feign flow
     * @param url      API url
     * @return Short answers API client
     */
    public static ShortAnswersApi shortAnswersApi(Logger.Level logLevel, String url) {
        return Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(logLevel)
                .target(ShortAnswersApi.class, url);
    }

    /**
     * Build Full results API client
     *
     * @return Full results API client
     */
    public static FullResultsApi fullResultsApi() {
        return fullResultsApi(Logger.Level.FULL, url);
    }

    /**
     * Build Full results API client
     *
     * @param logLevel logging level to use for internal feign flow
     * @return Full results API client
     */
    public static FullResultsApi fullResultsApi(Logger.Level logLevel) {
        return fullResultsApi(logLevel, url);
    }

    /**
     * Build Full results API client
     *
     * @param logLevel logging level to use for internal feign flow
     * @param url      API url
     * @return Full results API client
     */
    public static FullResultsApi fullResultsApi(Logger.Level logLevel, String url) {
        JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
                .withMarshallerJAXBEncoding(StandardCharsets.UTF_8.name())
                .build();

        return Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(logLevel)
                .decoder(new JAXBDecoder(jaxbFactory))
                .target(FullResultsApi.class, url);
    }
}
